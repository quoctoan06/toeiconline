package vn.myclass.controller;

import vn.myclass.core.common.constant.CoreConstant;
import vn.myclass.core.web.common.WebConstant;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

// class đọc file trong folder nằm ngoài project
public class DisplayImage extends HttpServlet {
    private final String imagesBase = "/" + CoreConstant.FOLDER_UPLOAD;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String imageUrl = request.getRequestURI();      // request từ edit.jsp là src = "/repository/${item.pojo.image}"
        String relativeImagePath = imageUrl.substring("/repository/".length());     // relativeImagePath sẽ có dạng /listenguideline/a.jpg
        ServletOutputStream outStream;
        outStream = response.getOutputStream();
        FileInputStream fin = new FileInputStream(imagesBase + File.separator + relativeImagePath);     // sẽ có dạng C:\fileupload\listenguideline\a.jpg
        // đoc file vào input stream và viết ra output stream
        BufferedInputStream bin = new BufferedInputStream(fin);
        BufferedOutputStream bout = new BufferedOutputStream(outStream);
        int ch = 0; ;
        while((ch = bin.read()) != -1)
            bout.write(ch);
        bin.close();
        fin.close();
        bout.close();
        outStream.close();
    }
}
