public class Main {
    public static void main(String[] args) {
        PayrollService payrollService = new PayrollService();

        System.out.println("=== Connecting to Database ===");
        payrollService.getConnection();

        System.out.println("\n=== Retrieving Employee Payroll ===");
        payrollService.retrieveEmployeePayroll().forEach(emp -> 
            System.out.println(emp.getName() + ": " + emp.getSalary())
        );

        System.out.println("\n=== Updating Salary ===");
        payrollService.updateSalary("Terisa", 3000000.00);

        System.out.println("\n=== Retrieving Employees by Date Range ===");
        payrollService.retrieveEmployeesByDateRange("2019-01-01", "2024-12-31").forEach(emp -> 
            System.out.println(emp.getName() + " started on " + emp.getStartDate())
        );

        System.out.println("\n=== Performing Gender Analysis ===");
        payrollService.performGenderAnalysis();
    }
}
