package application;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {
    private final TextField fullName = new TextField(), address = new TextField();
    private final TextField contact = new TextField(), email = new TextField();
    private final TextField education = new TextField(), position = new TextField();
    private final TextField salary = new TextField(), explanation = new TextField();
    private final TextField signature = new TextField();
    private final DatePicker date = new DatePicker();
    private final ToggleGroup gender = new ToggleGroup(), authorized = new ToggleGroup(), relatives = new ToggleGroup();

    private Label label(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size: 11px; -fx-text-fill: #222222;");
        l.setWrapText(true);
        return l;
    }
    private Label section(String title) {
        Label l = new Label(title);
        l.setPrefWidth(195);
        l.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 6 10 6 10;");
        return l;
    }
    private HBox row(double labelWidth, String title, Control control) {
        Label l = label(title);
        l.setMinWidth(labelWidth);
        l.setPrefWidth(labelWidth);
        HBox.setHgrow(control, Priority.ALWAYS);
        control.setMaxWidth(Double.MAX_VALUE);
        HBox h = new HBox(8, l, control);
        h.setAlignment(Pos.CENTER_LEFT);
        return h;
    }
    private HBox radios(ToggleGroup group, String... options) {
        HBox h = new HBox(14);
        for (String option : options) {
            RadioButton r = new RadioButton(option);
            r.setStyle("-fx-font-size: 11px;");
            r.setToggleGroup(group);
            h.getChildren().add(r);
        }
        return h;
    }
    private VBox yesNo(String question, ToggleGroup group) {
        VBox v = new VBox(5, label(question), radios(group, "Yes", "No"));
        return v;
    }
    private String selected(ToggleGroup group) {
        Toggle t = group.getSelectedToggle();
        return t == null ? null : ((RadioButton)t).getText();
    }
    private void message(Alert.AlertType type, String msg) {
        Alert a = new Alert(type, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }
    @Override public void start(Stage stage) {
        VBox page = new VBox(13);
        page.setPadding(new Insets(20, 26, 22, 26));
        page.setStyle("-fx-background-color: white; -fx-border-color: #222; -fx-border-width: 1;");

        Label star = new Label("★");
        star.setStyle("-fx-font-size: 27px; -fx-text-fill: black;");
        Label company = new Label("Ting Yang Chou");
        company.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");
        VBox brand = new VBox(1, company, label("Company"));
        HBox logo = new HBox(7, star, brand);
        logo.setAlignment(Pos.CENTER_LEFT);
        Label heading = new Label("Employment Application");
        heading.setAlignment(Pos.CENTER);
        heading.setMaxWidth(Double.MAX_VALUE);
        heading.setMinHeight(57);
        heading.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-size: 21px; -fx-font-weight: bold;");
        HBox header = new HBox(15, logo, heading);
        HBox.setHgrow(heading, Priority.ALWAYS);
        page.getChildren().addAll(header, section("Personal Information"));

        page.getChildren().addAll(row(135, "Full Name", fullName), row(135, "Current Address", address));
        HBox contactRow = new HBox(14, row(65, "Contact\nNumber", contact), row(58, "Email\nAddress", email));
        HBox.setHgrow(contactRow.getChildren().get(0), Priority.ALWAYS);
        HBox.setHgrow(contactRow.getChildren().get(1), Priority.ALWAYS);
        page.getChildren().add(contactRow);
        HBox educationRow = new HBox(14, row(65, "Highest\nEducational\nAttainment", education), new VBox(5, label("Gender"), radios(gender, "Male", "Female", "Other")));
        HBox.setHgrow(educationRow.getChildren().get(0), Priority.ALWAYS);
        page.getChildren().addAll(educationRow, section("Employment Eligibility"));

        HBox dateRow = new HBox(14, row(65, "Date\nAvailable", date), row(58, "Desired\nPosition", position));
        HBox.setHgrow(dateRow.getChildren().get(0), Priority.ALWAYS);
        HBox.setHgrow(dateRow.getChildren().get(1), Priority.ALWAYS);
        page.getChildren().addAll(dateRow, row(135, "Desired Salary", salary));
        page.getChildren().addAll(yesNo("Are you legally authorized to work in the country?", authorized),
                yesNo("Do you have relatives working for our company?", relatives),
                row(135, "If yes, please explain further", explanation));

        Region divider = new Region();
        divider.setMinHeight(1);
        divider.setStyle("-fx-background-color: #222;");
        Label agreement = label("By submitting this application, you agree\nto adhere to company policies and\nprovide accurate information throughout\nthe employment process.");
        HBox signRow = new HBox(12, agreement, row(73, "Applicant's\nSignature", signature));
        HBox.setHgrow(signRow.getChildren().get(1), Priority.ALWAYS);
        page.getChildren().addAll(divider, signRow);

        Button submit = new Button("Submit Application");
        submit.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 10 18;");
        submit.setOnAction(e -> save());
        HBox buttonRow = new HBox(submit);
        buttonRow.setAlignment(Pos.CENTER);
        page.getChildren().add(buttonRow);
        ScrollPane scroll = new ScrollPane(page);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: #eeeeee; -fx-padding: 14;");
        stage.setTitle("Ting Yang Chou - Employment Application");
        stage.setScene(new Scene(scroll, 610, 750));
        stage.show();
    }
    private void save() {
        if (fullName.getText().isBlank() || address.getText().isBlank() ||
                contact.getText().isBlank() || email.getText().isBlank() ||
                date.getValue() == null || position.getText().isBlank() ||
                selected(gender) == null || selected(authorized) == null ||
                selected(relatives) == null || signature.getText().isBlank()) {
            message(Alert.AlertType.WARNING, "Please complete all required fields.");
            return;
        }
        if ("Yes".equals(selected(relatives)) && explanation.getText().isBlank()) {
            message(Alert.AlertType.WARNING, "Please explain which relatives work for the company.");
            return;
        }
        BigDecimal amount = null;
        try {
            if (!salary.getText().isBlank()) {
                amount = new BigDecimal(salary.getText().trim().replace(",", ""));
                if (amount.signum() < 0) throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            message(Alert.AlertType.WARNING, "Desired Salary must be a non-negative number, for example 50000.");
            return;
        }
        String sql = "INSERT INTO employment_applications " +
            "(full_name, current_address, contact_number, email_address, highest_education, gender, " +
            "date_available, desired_position, desired_salary, legally_authorized, relatives_working, " +
            "relatives_explanation, applicant_signature) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fullName.getText().trim());
            ps.setString(2, address.getText().trim());
            ps.setString(3, contact.getText().trim());
            ps.setString(4, email.getText().trim());
            ps.setString(5, education.getText().trim());
            ps.setString(6, selected(gender));
            ps.setDate(7, Date.valueOf(date.getValue()));
            ps.setString(8, position.getText().trim());
            if (amount == null) ps.setNull(9, java.sql.Types.DECIMAL); else ps.setBigDecimal(9, amount);
            ps.setBoolean(10, "Yes".equals(selected(authorized)));
            ps.setBoolean(11, "Yes".equals(selected(relatives)));
            ps.setString(12, explanation.getText().trim());
            ps.setString(13, signature.getText().trim());
            if (ps.executeUpdate() == 1)
                message(Alert.AlertType.INFORMATION, "Application submitted successfully! Saved to MySQL.");
        } catch (SQLException e) {
            message(Alert.AlertType.ERROR, "Database error: " + e.getMessage());
        }
    }
    public static void main(String[] args) { launch(args); }
}
