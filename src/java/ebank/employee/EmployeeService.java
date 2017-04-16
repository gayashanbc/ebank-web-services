/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ebank.employee;

import ebank.utility.DB_Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author Shan
 */
@WebService(serviceName = "EmployeeService")
public class EmployeeService {

    /**
     * Validate login credentials of a particular employee
     *
     * @param username username to be validated
     * @param password password to be validated
     * @return whether the credentials are valid
     */
    @WebMethod(operationName = "validateLogin")
    public boolean validateLogin(@WebParam(name = "username") String username, @WebParam(name = "password") String password) {
        boolean isValid = false;
        String fetchedPassword = "";
        String query = "SELECT password FROM ebank_employee WHERE username='" + username + "'";
        ResultSet DB_Result = DB_Connection.fetchData(query);
        try {
            fetchedPassword = DB_Result.getString("password");
            System.out.println(fetchedPassword);
        } catch (Exception ex) { // no such user in the database
            Logger.getLogger(EmployeeService.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (password.equals(fetchedPassword)) { // credentials matched
            try { // update loggedIn status
                query = "UPDATE ebank_employee SET isLoggedIn=1 WHERE username = ?";
                PreparedStatement pst = DB_Connection.getInstance().prepareStatement(query);
                pst.setString(1, username);
                DB_Connection.runQuery(pst);
                isValid = true;
            } catch (Exception ex) {
                Logger.getLogger(EmployeeService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return isValid;
    }

    /**
     * Logout an employee
     *
     * @param username user to be logged out
     * @return whether the operation succeed
     */
    @WebMethod(operationName = "logout")
    public boolean logout(@WebParam(name = "username") String username) {
        String query = "SELECT isLoggedIn FROM ebank_employee WHERE username='" + username + "'";
        ResultSet DB_Result = DB_Connection.fetchData(query);
        try {
            int isLoggedIn = DB_Result.getInt("isLoggedIn");
            if (isLoggedIn == 0) { // user is currently not logged in
                return false;
            } else {
                query = "UPDATE ebank_employee SET isLoggedIn=0 WHERE username = ?";
                PreparedStatement pst = DB_Connection.getInstance().prepareStatement(query);
                pst.setString(1, username);
                DB_Connection.runQuery(pst);
            }
        } catch (Exception ex) {
            Logger.getLogger(EmployeeService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;

    }

    /**
     * Delete an employee record from the database
     * @param employeeId employeeId of the employee that needs to be deleted
     * @return whether the operation succeed
     */
    @WebMethod(operationName = "deleteEmployee")
    public boolean deleteEmployee(@WebParam(name = "employeeId") int employeeId) {
        boolean isValid = false;
        try {
            String query = "DELETE FROM ebank_employee WHERE employeeId = ?";
            PreparedStatement pst = DB_Connection.getInstance().prepareStatement(query);
            pst.setInt(1, employeeId);
            DB_Connection.runQuery(pst);
            isValid = true;
        } catch (Exception ex) {
            Logger.getLogger(EmployeeService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isValid;
    }

    /**
     * Get employeeId and name of each employee from the database
     *
     * @return employee records
     */
    @WebMethod(operationName = "getEmployees")
    public ArrayList<String> getEmployees() {
        ArrayList<String> employeeRecords = new ArrayList<>();
        try {
            String query = "SELECT employeeId,name FROM ebank_employee";
            ResultSet DB_Result = DB_Connection.fetchData(query);

            do { // add records to an ArrayList
                String[] data = new String[2];
                data[0] = DB_Result.getInt("employeeId") + "";
                data[1] = DB_Result.getString("name");
                employeeRecords.add(data[0] + ";" + data[1]);
            } while (DB_Result.next());

        } catch (SQLException ex) {
            Logger.getLogger(EmployeeService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return employeeRecords;
    }

    /**
     * Get details of a particular employee
     *
     * @param employeeId record to be retrieved
     * @return employee details
     */
    @WebMethod(operationName = "getEmployee")
    public String[] getEmployee(@WebParam(name = "employeeId") int employeeId) {
        String[] employeeRecord = new String[5];
        for (int i = 0; i < employeeRecord.length; i++) {
            employeeRecord[i] = "";
        }
        try {
            String query = "SELECT * FROM ebank_employee WHERE employeeId=" + employeeId;
            ResultSet DB_Result = DB_Connection.fetchData(query);
            employeeRecord[0] = DB_Result.getInt(1) + "";
            for (int i = 1; i < employeeRecord.length; i++) {
                employeeRecord[i] = DB_Result.getString((i + 1));
            }

        } catch (SQLException ex) {
            Logger.getLogger(EmployeeService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return employeeRecord;
    }

    /**
     * Add/Edit employee record to/from the database
     * @param employeeId
     * @param name
     * @param position
     * @param username
     * @param password
     * @return whether the operation succeed
     */
    @WebMethod(operationName = "addEditEmployee")
    public boolean addEditEmployee(@WebParam(name = "employeeId") int employeeId, @WebParam(name = "name") String name, @WebParam(name = "position") String position, @WebParam(name = "username") String username, @WebParam(name = "password") String password) {
        String query = "";
        boolean isSuccess = false;
        try {
            if (employeeId == 0) { // new employee record
                query = "INSERT INTO ebank_employee(name,position,username,password) VALUES(?,?,?,?)";
                PreparedStatement pst = DB_Connection.getInstance().prepareStatement(query);
                pst.setString(1, name);
                pst.setString(2, position);
                pst.setString(3, username);
                pst.setString(4, password);
                DB_Connection.runQuery(pst);
                isSuccess = true;
            } else { // edit an existing employee record
                query = "UPDATE ebank_employee SET name=?,position=?,username=?,password=? WHERE employeeId = ?";
                PreparedStatement pst = DB_Connection.getInstance().prepareStatement(query);
                pst.setString(1, name);
                pst.setString(2, position);
                pst.setString(3, username);
                pst.setString(4, password);
                pst.setInt(5, employeeId);
                DB_Connection.runQuery(pst);
                isSuccess = true;
            }

        } catch (Exception ex) {
            Logger.getLogger(EmployeeService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isSuccess;
    }
}
