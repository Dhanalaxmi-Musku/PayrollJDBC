import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PayrollService {
    private static final String URL = "jdbc:mysql://localhost:3306/payroll_service";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    


    public Connection getConnection() {
        Connection connection = null;
        

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection Established.");
        } catch (SQLException e) {
            throw new CustomSQLException("Error establishing database connection.", e);
        }
        return connection;
    }

    public List<EmployeePayroll> retrieveEmployeePayroll() {
        List<EmployeePayroll> employees = new ArrayList<>();
        String query = "SELECT * FROM employee_payroll";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                EmployeePayroll employee = new EmployeePayroll();
                employee.setId(resultSet.getInt("id"));
                employee.setName(resultSet.getString("name"));
                employee.setSalary(resultSet.getDouble("salary"));
                employee.setStartDate(resultSet.getDate("start_date"));
                employees.add(employee);
            }
        }  catch (SQLException e) {
            throw new CustomSQLException("Error retrieving employee payroll data.", e);
        }
        return employees;
    }

    public void updateSalary(String name, double newSalary) {
        String query = "UPDATE employee_payroll SET salary = ? WHERE name = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setDouble(1, newSalary);
            preparedStatement.setString(2, name);
            int rowsUpdated = preparedStatement.executeUpdate();
            System.out.println("Rows updated: " + rowsUpdated);
        } catch (SQLException e) {
            throw new CustomSQLException("Error retrieving employee payroll data.", e);
        }
    }

    public List<EmployeePayroll> retrieveEmployeesByDateRange(String startDate, String endDate) {
        List<EmployeePayroll> employees = new ArrayList<>();
        String query = "SELECT * FROM employee_payroll WHERE start_date BETWEEN ? AND ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setDate(1, Date.valueOf(startDate));
            preparedStatement.setDate(2, Date.valueOf(endDate));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                EmployeePayroll employee = new EmployeePayroll();
                employee.setId(resultSet.getInt("id"));
                employee.setName(resultSet.getString("name"));
                employee.setSalary(resultSet.getDouble("salary"));
                employee.setStartDate(resultSet.getDate("start_date"));
                employees.add(employee);
            }
        } catch (SQLException e) {
            throw new CustomSQLException("Error retrieving employee payroll data.", e);
        }
        return employees;
    }

    public void performGenderAnalysis() {
        String query = "SELECT gender, SUM(salary) AS total_salary, AVG(salary) AS avg_salary, " +
                       "MIN(salary) AS min_salary, MAX(salary) AS max_salary, COUNT(*) AS employee_count " +
                       "FROM employee_payroll GROUP BY gender";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                System.out.println("Gender: " + resultSet.getString("gender") +
                                   ", Total Salary: " + resultSet.getDouble("total_salary") +
                                   ", Average Salary: " + resultSet.getDouble("avg_salary") +
                                   ", Min Salary: " + resultSet.getDouble("min_salary") +
                                   ", Max Salary: " + resultSet.getDouble("max_salary") +
                                   ", Employee Count: " + resultSet.getInt("employee_count"));
            }
        } catch (SQLException e) {
            throw new CustomSQLException("Error retrieving employee payroll data.", e);
        }
    }
}