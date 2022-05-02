package com.uat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.json.JSONArray;


/**
 *
 * @author louis
 */
@MultipartConfig
@WebServlet("/upload") 
public class MyServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet MyServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet MyServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    /**
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getMethod().equalsIgnoreCase("PATCH")){
           doPatch(request, response);
        } else {
            super.service(request, response);
        }
    }
    
    private static final int BUFFER_SIZE = 4096; // 4KB

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        // declare
        HashMap hm_temp = new HashMap();
        ArrayList al_result = new ArrayList();
        ArrayList al_Files = new ArrayList();
        boolean isDirectory = false;
        byte[] bytes = {};
        String errMSG = "";
        String errCODE = "";
        String localSystemFilePath = "";
        String orderBy = "";
        String orderByDirection = "";
        String filterByName = "";
        String outputFilePath = "C:/Users/louis/Desktop/20220412/outputFile.txt";        
        String queryString = request.getQueryString();
                
        // start
        try {
            
        // split the queryString
        String [] splitString = queryString.split("&");
        // get localSystemFilePath & orderby & filter
        localSystemFilePath = splitString[0];
        orderBy = splitString[1].replace("orderBy=", "");
        orderByDirection = splitString[2].replace("orderByDirection=", "");
        filterByName = splitString[3].replace("filterByName=", "");
                    
        // read File
        File inputFile = new File(localSystemFilePath);
        // check Directory or File
        isDirectory = inputFile.isDirectory();
        if(isDirectory){
            String[] FileList = inputFile.list();
            int FilesLength = inputFile.list().length;
            if(FilesLength==0){
                errMSG += "Directory is empty.";
            }else{
                // add FilesName
                for (int i=0; i<FileList.length; i++) {                    
                    al_Files.add(i, FileList[i]);
                }
            }
        }else{
            String inputFilePath = localSystemFilePath;
            // IO FileReader   
            InputStream inputStream = new FileInputStream(inputFilePath);            
            OutputStream outputStream = new FileOutputStream(outputFilePath);
            // ByteArray
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            Writer out = new OutputStreamWriter(byteOut, "UTF-8");  // Uses UTF-8 encoding
            
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;
            
            while ((bytesRead = inputStream.read(buffer)) != -1) {                
                outputStream.write(buffer, 0, bytesRead); // write original context
                out.write(bytesRead);                
            }
            
            // close IO Stream
            inputStream.close();
            outputStream.close();
            out.close(); 
            
            bytes = byteOut.toByteArray();
        }
                
        } catch (Exception err) {
            errMSG += err.getMessage();
            errCODE += HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }
                
        // build return message
        if(errMSG!="") hm_temp.put("ErrorMessage", errMSG);
        if(errCODE!="") hm_temp.put("ErrorCode", errCODE);
        if(!al_Files.isEmpty()) hm_temp.put("Files", al_Files);
        hm_temp.put("binary stream", bytes);
        hm_temp.put("isDirectory", isDirectory);
        
        // add to ArrayList
        al_result.add(hm_temp);
        
        // ArrayList to JSONArray
        JSONArray mJSONArray = new JSONArray(al_result);
        // return JSONArray
        response.getWriter().write(mJSONArray.toString());
    }
    
    
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8"); 
        
        // declare
        HashMap hm_temp = new HashMap();
        ArrayList al_result = new ArrayList();
        JSONArray mJSONArray = new JSONArray();        
        String errMSG = "";
        String errCODE = "";
            
        // build return message
        hm_temp.put("RESULT", "這是Post測試結果");
        hm_temp.put("ITEMS", "DATAPARAM");
        hm_temp.put("MSG", "000");        
        if(errMSG!="") hm_temp.put("ErrorMessage", errMSG);
        if(errCODE!="") hm_temp.put("ErrorCode", errCODE);
        al_result.add(hm_temp);        
         
        // add to JSONArray
        mJSONArray.put(al_result);
        // return JSONArray
        response.getWriter().write(mJSONArray.toString());
    }
    

    public void doPatch(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        // declare
        HashMap hm_temp = new HashMap();        
        ArrayList al_result = new ArrayList();        
        String errMSG = "";
        String errCODE = "";
        
        
        try {
                        
        } catch (Exception err) {
            errMSG += err.getMessage();
            errCODE += HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }
        
        
        // build return message
        if(errMSG!="") hm_temp.put("ErrorMessage", errMSG);
        if(errCODE!="") hm_temp.put("ErrorCode", errCODE);
        hm_temp.put("return", "doPatch success");
        
        // add to ArrayList
        al_result.add(hm_temp);
        
        // ArrayList to JSONArray
        JSONArray mJSONArray = new JSONArray(al_result);
        // return JSONArray
        response.getWriter().write(mJSONArray.toString());       
    }
    
    
    public void doDelete(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        // declare
        HashMap hm_temp = new HashMap();        
        ArrayList al_result = new ArrayList();        
        String errMSG = "";
        String errCODE = "";
        String localSystemFilePath = "";        
        String queryString = request.getQueryString();
                        
        try {
            
            // split the queryString
            String [] splitString = queryString.split("&");
            // get localSystemFilePath & orderby & filter
            localSystemFilePath = splitString[0];

            File delFile = new File(localSystemFilePath);                 
            boolean isFileExist = Files.deleteIfExists(delFile.toPath());
            if(isFileExist){
                delFile.delete();
                errMSG += "File Delete Success.";            
            }else{
                errMSG += "File does not exist.";
            }
        
        } catch (Exception err) {
            errMSG += err.getMessage();
            errCODE += HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }
        
        // build return message
        if(errMSG!="") hm_temp.put("ErrorMessage", errMSG);
        if(errCODE!="") hm_temp.put("ErrorCode", errCODE);        
        
        // add to ArrayList
        al_result.add(hm_temp);
        
        // ArrayList to JSONArray
        JSONArray mJSONArray = new JSONArray(al_result);
        // return JSONArray
        response.getWriter().write(mJSONArray.toString());       
    }
    
    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
