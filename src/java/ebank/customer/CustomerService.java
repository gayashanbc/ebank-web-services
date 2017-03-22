/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ebank.customer;

import ebank.employee.EmployeeService;
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
@WebService(serviceName = "CustomerService")
public class CustomerService {

    /**
     * Web service operation
     *
     * @return customer records
     */
    @WebMethod(operationName = "getCustomers")
    public ArrayList<String> getCustomers() {
        ArrayList<String> customerRecords = new ArrayList<>();
        try {
            String query = "SELECT name,accountNo FROM ebank_customer";
            ResultSet DB_Result = DB_Connection.fetchData(query);

            do {
                String[] data = new String[2];
                data[0] = DB_Result.getString("name") + "";
                data[1] = DB_Result.getString("accountNo");
                customerRecords.add(data[0] + ";" + data[1]);
            } while (DB_Result.next());

        } catch (SQLException ex) {
            Logger.getLogger(EmployeeService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return customerRecords;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "deleteCustomer")
    public boolean deleteCustomer(@WebParam(name = "accountNo") String accountNo) {
        boolean isValid = false;
        try {
            String query = "DELETE FROM ebank_customer WHERE accountNo = ?";
            PreparedStatement pst = DB_Connection.getInstance().prepareStatement(query);
            pst.setString(1, accountNo);
            DB_Connection.runQuery(pst);
            isValid = true;
        } catch (Exception ex) {
            Logger.getLogger(EmployeeService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isValid;
    }

    /**
     * Web service operation
     *
     * @param accountNo
     * @return customer
     */
    @WebMethod(operationName = "getCustomer")
    public String[] getCustomer(@WebParam(name = "customerId") String accountNo) {
        String[] customerRecord = new String[11];
        for (int i = 0; i < customerRecord.length; i++) {
            customerRecord[i] = "";
        }
        try {
            String query = "SELECT * FROM ebank_customer WHERE accountNo='" + accountNo + "'";
            ResultSet DB_Result = DB_Connection.fetchData(query);
            customerRecord[0] = DB_Result.getInt(1) + "";
            for (int i = 1; i < customerRecord.length; i++) {
                customerRecord[i] = DB_Result.getString((i + 1));
            }

        } catch (SQLException ex) {
            Logger.getLogger(EmployeeService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return customerRecord;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "addEditCustomer")
    public boolean addEditCustomer(@WebParam(name = "customerId") int customerId, @WebParam(name = "name") String name, @WebParam(name = "birthdate") String birthdate, @WebParam(name = "address") String address, @WebParam(name = "mobile") String mobile, @WebParam(name = "email") String email, @WebParam(name = "type") String type, @WebParam(name = "accountNo") String accountNo, @WebParam(name = "sortCode") String sortCode, @WebParam(name = "balance") String balance, @WebParam(name = "card") String card) {
        String query = "";
        boolean isSuccess = false;
        try {
            if (customerId == 0) {
                query = "INSERT INTO ebank_customer(name,birthdate,address,mobile,email,type,accountNo,sortCode,balance,card) VALUES(?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement pst = DB_Connection.getInstance().prepareStatement(query);
                pst.setString(1, name);
                pst.setString(2, birthdate);
                pst.setString(3, address);
                pst.setString(4, mobile);
                pst.setString(5, email);
                pst.setString(6, type);
                pst.setString(7, accountNo);
                pst.setString(8, sortCode);
                pst.setString(9, balance);
                pst.setString(10, card);
                DB_Connection.runQuery(pst);
                isSuccess = true;
            } else {
                query = "UPDATE ebank_customer SET name=?,birthdate=?,address=?,mobile=?,email=?,type=?,accountNo=?,sortCode=?,balance=?,card=? WHERE customerId = ?";
                PreparedStatement pst = DB_Connection.getInstance().prepareStatement(query);
                pst.setString(1, name);
                pst.setString(2, birthdate);
                pst.setString(3, address);
                pst.setString(4, mobile);
                pst.setString(5, email);
                pst.setString(6, type);
                pst.setString(7, accountNo);
                pst.setString(8, sortCode);
                pst.setString(9, balance);
                pst.setString(10, card);
                pst.setInt(11, customerId);
                DB_Connection.runQuery(pst);
                isSuccess = true;
            }

        } catch (Exception ex) {
            Logger.getLogger(EmployeeService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isSuccess;
    }

}
