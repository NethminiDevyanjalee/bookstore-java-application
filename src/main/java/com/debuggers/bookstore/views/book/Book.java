package com.debuggers.bookstore.views.book;

import com.cloudinary.Url;
import com.debuggers.bookstore.models.*;
import com.debuggers.bookstore.repository.DataRepository;
import com.debuggers.bookstore.repository.DataRepositoryException;
import com.debuggers.bookstore.services.upload.FileUploader;
import com.debuggers.bookstore.services.upload.FileUploaderException;
import com.debuggers.bookstore.services.upload.cloudinary.CloudinaryFileUploader;
import com.debuggers.bookstore.views.Alert;
import com.debuggers.bookstore.views.PageView;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class Book extends PageView {
    private DataRepository dataRepository;
    private JPanel mainPanel;
    private JPanel formPanel;
    private JTextField txtName;
    private JTextField txtISBN;
    private JTextField txtPrice;
    private JButton btnSave;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnClear;
    private JTable table;
    private JScrollPane jScrollPane;
    private JComboBox comboLanguage;
    private JComboBox comboAuthor;
    private JComboBox comboPublisher;
    private JComboBox comboCategory;
    private JComboBox comboSubCategory;
    private JTextArea textareaDescription;
    private JLabel imgViwer;

    private List<SqlDataModel> dataList;
    private List<SqlDataModel> authorList;
    private List<SqlDataModel> publisherList;
    private List<SqlDataModel> languageList;
    private List<SqlDataModel> categoryList;
    private BookModel bookModel;
    private File selectedFile;

    private FileUploader fileUploader;

    private String fetchCategoriesQuery = """
            SELECT category,book_category.id,book_category.is_delete,
            IFNULL(GROUP_CONCAT(sub_category),'N/A') AS 'sub_category',
            IFNULL(GROUP_CONCAT(book_sub_category.id),'N/A') AS 'sub_category_ids'
            FROM book_category
            LEFT JOIN book_sub_category
            ON book_sub_category.category_id = book_category.id AND book_sub_category.is_delete = 0
            WHERE book_category.is_delete = 0
            GROUP BY book_category.id
            """;
    private String bookFetchQuery = """
            SELECT
            book.id,book.name AS NAME,price,isbn,category_id,sub_category_id,language_id,author_id,publisher_id,description,img_url,
            CONCAT(book_author.fname,' ',book_author.lname) AS author,
            book_publisher.name AS publisher,
            book_language.language
            FROM book
            INNER JOIN book_author ON book_author.id = book.author_id
            INNER JOIN book_publisher ON book_publisher.id = book.publisher_id
            INNER JOIN book_language ON book_language.id = book.language_id
            WHERE book.is_delete = '0'
            """;
    private String findISBNExist = "SELECT id FROM book WHERE isbn = %s";

    public Book(DataRepository dataRepository) {
        super();
        this.dataRepository = dataRepository;
        this.fileUploader = new CloudinaryFileUploader();
        initComponents();
    }

    private void initComponents() {
        add(mainPanel);
        loadLanguage();
        loadAuthor();
        loadPublisher();
        loadCategory();
        createTable();
        initDefaultImageView();

        btnClear.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                clearFields();
            }
        });
        btnDelete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                delete();
            }
        });
        btnEdit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                edit();
            }
        });
        btnSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                insert();
            }
        });
        comboCategory.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                loadSubCategory();
            }
        });
        imgViwer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                popUpImageSelector();
            }
        });

    }

    private void initDefaultImageView() {
        setImageViewImage(new File("src/main/resources/images/upload.png"));
    }

    private void setImageViewImage(File file) {
        try {
            imgViwer.setIcon(
                    new ImageIcon(
                            getScaledImage(
                                    ImageIO.read(file),
                                    100,
                                    100
                            )
                    )
            );
        } catch (IOException e) {
            Alert.showError("System Error:", e.getMessage());
        }
    }

    private void setImageViewImage(String url) {
        try {
            imgViwer.setIcon(
                    new ImageIcon(
                            getScaledImage(
                                    ImageIO.read(new URL(url)),
                                    100,
                                    100
                            )
                    )
            );
        } catch (IOException e) {
            Alert.showError("System Error:", e.getMessage());
        }
    }

    private void loadLanguage() {

        dataRepository.createStatement("book_language");

        try {

            languageList = dataRepository.get(BookLanguageModel.class);
            comboLanguage.addItem("Choose");
            languageList.stream().forEach((e) -> {
                BookLanguageModel language = (BookLanguageModel) e;
                comboLanguage.addItem(language.getLanguage());
            });

        } catch (DataRepositoryException e) {

            Alert.showError("Database Error:", e.getMessage());

        }
    }

    private void loadAuthor() {

        dataRepository.createStatement("book_author");
        dataRepository.where("is_delete", 0);

        try {

            authorList = dataRepository.get(BookAuthorModel.class);
            comboAuthor.addItem("Choose");
            authorList.stream().forEach((e) -> {
                BookAuthorModel author = (BookAuthorModel) e;
                comboAuthor.addItem(author.getFirstName() + " " + author.getLastName());
            });

        } catch (DataRepositoryException e) {

            Alert.showError("Database Error:", e.getMessage());

        }
    }

    private void loadPublisher() {

        dataRepository.createStatement("book_publisher");
        dataRepository.where("is_delete", 0);

        try {

            publisherList = dataRepository.get(BookPublisherModel.class);
            comboPublisher.addItem("Choose");
            publisherList.stream().forEach((e) -> {
                BookPublisherModel publisher = (BookPublisherModel) e;
                comboPublisher.addItem(publisher.getName());
            });

        } catch (DataRepositoryException e) {

            Alert.showError("Database Error:", e.getMessage());

        }
    }

    private void loadCategory() {

        try {

            categoryList = dataRepository.executeQuery(fetchCategoriesQuery, CategoryModel.class);
            comboCategory.addItem("Choose");
            categoryList.stream().forEach((e) -> {
                CategoryModel category = (CategoryModel) e;
                comboCategory.addItem(category.getCategory());
            });

        } catch (DataRepositoryException e) {

            Alert.showError("Database Error:", e.getMessage());

        }
    }

    private void loadSubCategory() {
        comboSubCategory.removeAllItems();
        int selectedIndex = comboCategory.getSelectedIndex() - 1;
        if (selectedIndex < 0) {
            comboSubCategory.addItem("N/A");
            return;
        }
        List<String> subs = ((CategoryModel) categoryList.get(selectedIndex)).getSubCategoriesList();
        if (subs.isEmpty()) {
            comboSubCategory.addItem("N/A");
        } else {
            comboSubCategory.addItem("Choose");
            subs.forEach((e) -> comboSubCategory.addItem(e));
        }

    }

    void popUpImageSelector() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            setImageViewImage(selectedFile);
        }
    }

    private void insert() {

        if (bookModel == null)
            bookModel = new BookModel();

        bookModel.setName(txtName.getText());
        bookModel.setDescription(textareaDescription.getText());
        bookModel.setPrice(Double.parseDouble(txtPrice.getText()));
        bookModel.setIsbn(txtISBN.getText());

        if (bookModel.getName().isEmpty()) {
            Alert.showError("Validation Error:", "Name is required!");
            return;
        }
        if (bookModel.getIsbn().isEmpty()) {
            Alert.showError("Validation Error:", "ISBN is required!");
            return;
        }
        if (bookModel.getPrice() == 0) {
            Alert.showError("Validation Error:", "Price is required!");
            return;
        }
        if (bookModel.getDescription().isEmpty()) {
            Alert.showError("Validation Error:", "Description is required!");
            return;
        }
        if (comboAuthor.getSelectedIndex() == 0) {
            Alert.showError("Validation Error:", "Select the Author!");
            return;
        }
        if (comboPublisher.getSelectedIndex() == 0) {
            Alert.showError("Validation Error:", "Select the Publisher!");
            return;
        }
        if (comboLanguage.getSelectedIndex() == 0) {
            Alert.showError("Validation Error:", "Select the Language!");
            return;
        }
        if (comboCategory.getSelectedIndex() == 0) {
            Alert.showError("Validation Error:", "Select the Category!");
            return;
        }
        if (comboSubCategory.getModel().getSize() > 0 && comboSubCategory.getSelectedIndex() == 0) {
            Alert.showError("Validation Error:", "Select the Sub Category!");
            return;
        }

        BookAuthorModel selectedAuthor = (BookAuthorModel) authorList.get(comboAuthor.getSelectedIndex() - 1);
        BookPublisherModel selectedPublisher = (BookPublisherModel) publisherList.get(comboPublisher.getSelectedIndex() - 1);
        BookLanguageModel selectedLanguage = (BookLanguageModel) languageList.get(comboLanguage.getSelectedIndex() - 1);
        CategoryModel selectedCategory = (CategoryModel) categoryList.get(comboCategory.getSelectedIndex() - 1);
        bookModel.setAuthorId(selectedAuthor.getId());
        bookModel.setLanguageId(selectedLanguage.getId());
        bookModel.setPublisherId(selectedPublisher.getId());
        bookModel.setCategoryId(selectedCategory.getId());

        if (!selectedCategory.getSubCategoriesIDSList().isEmpty()) {
            int subCatId = selectedCategory.getSubCategoriesIDSList().get(comboSubCategory.getSelectedIndex() - 1);
            bookModel.setSubCategoryId(subCatId);
        }
        dataRepository.createStatement("book");
        dataRepository.where("isbn", bookModel.getIsbn());
        try {

            dataRepository.createStatement("book");
            ResultSet resultSet = dataRepository.executeQuery(findISBNExist.formatted(bookModel.getIsbn()));
            if (bookModel.getId() == 0) {
                if (resultSet.next()) {
                    Alert.showError("Validation Error:", "This ISBN is already exist!");
                    return;
                }
                if (selectedFile != null) {
                    bookModel.setImageURL(fileUploader.upload(selectedFile));
                }
                dataRepository.insert(bookModel);
            } else {
                if (resultSet.next() && resultSet.getInt("id") != bookModel.getId()) {
                    Alert.showError("Validation Error:", "This ISBN is already exist!");
                    return;
                }
                if (selectedFile != null) {
                    bookModel.setImageURL(fileUploader.upload(selectedFile));
                }
                dataRepository.where("id", bookModel.getId());
                dataRepository.update(bookModel);
            }
            Alert.showSuccess("Success", "This change has been saved!");
            createTable();
            clearFields();
            bookModel = null;

        } catch (DataRepositoryException | SQLException | FileUploaderException exception) {
            Alert.showError("Database Error:", exception.getMessage());
        }

    }


    private void edit() {
        final int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            Alert.showError("Edit Error:", "Please select the record that you want to edit!");
            return;
        }

        bookModel = (BookModel) dataList.get(selectedRow);

        txtName.setText(bookModel.getName());
        txtISBN.setText(bookModel.getIsbn());
        txtPrice.setText(String.valueOf(bookModel.getPrice()));
        textareaDescription.setText(bookModel.getDescription());
        setImageViewImage(bookModel.getImageURL());
        BookAuthorModel authorModel = (BookAuthorModel) authorList.stream().filter((e) -> ((BookAuthorModel) e).getId() == bookModel.getAuthorId()).findFirst().get();
        BookPublisherModel publisherModel = (BookPublisherModel) publisherList.stream().filter((e) -> ((BookPublisherModel) e).getId() == bookModel.getPublisherId()).findFirst().get();
        BookLanguageModel languageModel = (BookLanguageModel) languageList.stream().filter((e) -> ((BookLanguageModel) e).getId() == bookModel.getLanguageId()).findFirst().get();
        CategoryModel categoryModel = (CategoryModel) categoryList.stream().filter((e) -> ((CategoryModel) e).getId() == bookModel.getCategoryId()).findFirst().get();

        comboAuthor.setSelectedIndex(authorList.indexOf(authorModel) + 1);
        comboPublisher.setSelectedIndex(publisherList.indexOf(publisherModel) + 1);
        comboLanguage.setSelectedIndex(languageList.indexOf(languageModel) + 1);
        comboCategory.setSelectedIndex(categoryList.indexOf(categoryModel) + 1);

        if (!categoryModel.getSubCategoriesIDSList().isEmpty()) {
            int index = categoryModel.getSubCategoriesIDSList().indexOf(bookModel.getSubCategoryId());
            comboSubCategory.setSelectedIndex(index + 1);
        }


    }

    private void delete() {
        final int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            Alert.showError("Deletion Error:", "Please select the record that you want to delete!");
            return;
        }

        final int response = Alert.showConfirm("Delete record", "Are you sure you want to delete?");
        bookModel = (BookModel) dataList.get(selectedRow);
        bookModel.setIsDelete(1);

        if (response == 0) {

            try {

                dataRepository.createStatement("book");
                dataRepository.where("id", bookModel.getId());
                dataRepository.update(bookModel);
                bookModel = null;
                createTable();

            } catch (DataRepositoryException exception) {

                Alert.showError("Deletion Error:", exception.getMessage());

            }
        }

    }

    private void clearFields() {
        Arrays.stream(formPanel.getComponents()).forEach((c) -> {
            if (c instanceof JTextField) {
                ((JTextField) c).setText(null);
            }
            if (c instanceof JTextArea) {
                ((JTextArea) c).setText(null);
            }
            if (c instanceof JComboBox) {
                ((JComboBox) c).setSelectedIndex(0);
            }
        });
        initDefaultImageView();
        bookModel = null;
        selectedFile = null;
    }

    private void createTable() {
        final String[] columnNames = {"Name", "ISBN", "Author", "Publisher", "Language", "Price"};
        final DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        try {

            dataList = dataRepository.executeQuery(bookFetchQuery, BookModel.class);

            for (var row : dataList) {
                BookModel data = (BookModel) row;
                tableModel.addRow(new String[]{
                        data.getName(),
                        String.valueOf(data.getIsbn()),
                        data.getAuthor(),
                        data.getPublisher(),
                        data.getLanguage(),
                        String.valueOf(data.getPrice())
                });
            }

        } catch (DataRepositoryException e) {
            Alert.showError("Database Error:", e.getMessage());
        }

        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        jScrollPane.setViewportView(table);
    }

    private Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }

}

