package ru.gb.netty.fima.client;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import ru.gb.netty.fima.autReg.DownloadFileRequest;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ServerFilePanelController implements Initializable {
    @FXML
    public TextField pathField;
    @FXML
    public TableView<FileInfo> fileTable;

    private Connect connect;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ControllerRegistry.register(this);
        //Создание столбца
        TableColumn<FileInfo, String> fileTypeColumn = new TableColumn<>();
        fileTypeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType().getName()));
        fileTypeColumn.setPrefWidth(24);

        TableColumn<FileInfo, String> filenameColumn = new TableColumn<>("Имя");
        filenameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFilename()));
        filenameColumn.setPrefWidth(160);

        TableColumn<FileInfo, Long> fileSizeColumn = new TableColumn<>("Размер");
        fileSizeColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getSize()));
        // Отвечает за то, как выглядит ячейка в столбце
        fileSizeColumn.setCellFactory(column -> {
            return new TableCell<FileInfo, Long>(){
                @Override
                protected void updateItem(Long item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        String text = String.format("%,d bytes", item);
                        if(item == -1L) {
                            text = "[DIR]";
                        }
                        setText(text);
                    }
                }
            };
        });
        fileSizeColumn.setPrefWidth(80);

        //Столбец для показа даты и времени
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");   // Для показа времени дописать HH:mm:ss
        TableColumn<FileInfo, String> fileDataColumn = new TableColumn<>("Дата изменения");
        fileDataColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLastModified().format(dtf)));
        fileDataColumn.setPrefWidth(80);


        // Добавление столбцов
        fileTable.getColumns().addAll(fileTypeColumn, filenameColumn,fileSizeColumn,fileDataColumn);

        //Сортировка по первому столбцу
        fileTable.getSortOrder().add(fileTypeColumn);


        //Переход по директориям
        fileTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    Path path = Paths.get(pathField.getText()).resolve(fileTable.getSelectionModel().getSelectedItem().getFilename());
                    if (Files.isDirectory(path)) {
                        updateList(path);
                    }
                }
            }
        });

        PrimaryController pr=
                (PrimaryController) ControllerRegistry.getControllerObject(PrimaryController.class);
        updateList(Path.of("src/main/clients.directory/", pr.AuthLogin.getText().trim()));

    }


    public void updateList(Path path) {
        try {
            pathField.setText(path.normalize().toAbsolutePath().toString());
            fileTable.getItems().clear();           // Для отчистки одержимого
            fileTable.getItems().addAll(Files.list(path).map(FileInfo::new).collect(Collectors.toList()));
            //Сортировка таблицы по молчанию
            fileTable.sort();
        } catch (IOException e) {
            //Создание всплывающего окна об ошибке если по какой-то причине не удалось получить список файлов
            Alert alert = new Alert(Alert.AlertType.WARNING, "По какой-то причине не удалось обновить список файлов", ButtonType.OK);
            //Ждёт пока пользователь не ткнёт кнопку ОК
            alert.showAndWait();
        }
    }

    @FXML
    public void clickBtnDownload(ActionEvent actionEvent) {

        if(fileTable.getSelectionModel().getSelectedItem().getFileName() == null){
            System.out.println("файл не выбран");
        } else {
            System.out.println(fileTable.getSelectionModel().getSelectedItem().getFileName());
            System.out.println(fileTable.getSelectionModel().getSelectedItem().getPath());
        }

        DownloadFileRequest dfr =
                new DownloadFileRequest(fileTable.getSelectionModel().getSelectedItem().getPath());
        PrimaryController pr =
                (PrimaryController) ControllerRegistry.getControllerObject(PrimaryController.class);

        connect = pr.getConnect();
        connect.getChannel().writeAndFlush(dfr);
    }

    // Кнопка вверх
    @FXML
    public void btnUpAction(ActionEvent actionEvent) {
        Path upperPath = Paths.get(pathField.getText()).getParent();
        if (upperPath != null) {
            updateList(upperPath);
        }
    }

    public void btnUpdateFileList(ActionEvent actionEvent) {
        updateList(Path.of(pathField.getText()));
    }

    public String getSelectedFilename() {
        if (!fileTable.isFocused()) {
            return null;
        }
        return fileTable.getSelectionModel().getSelectedItem().getFilename();
    }

    public String getCurrentPath() {
        return pathField.getText();
    }

}