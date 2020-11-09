package org.gmdev.pdftrick.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.gmdev.pdftrick.manager.PdfTrickBag;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class MergeFiles {
	
	private static final Logger logger = Logger.getLogger(MergeFiles.class);
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	
	/**
	 * Merge multiple pdf files
	 */
	public File mergePdf (ArrayList<File> filesVett, Path pdfFilePath) {
		final HashMap<String, String> namePwd = BAG.getNamePwd();
		
		File mergedFile = pdfFilePath.toFile();
		List<StreamPwdContainer> list = new ArrayList<StreamPwdContainer>();
        
		try {
        	// Source pdfs
        	Iterator<File> ite = filesVett.iterator();
			while (ite.hasNext()) {
				File element = ite.next();
				StreamPwdContainer boom = new StreamPwdContainer();
				boom.setIn(new FileInputStream(element));
				
				if (namePwd.containsKey(element.getName())) {
					boom.setPwd(namePwd.get(element.getName()));
				} else {
					boom.setPwd("");
				}
				
				list.add(boom);
			}
			// Resulting pdf
            OutputStream out = new FileOutputStream(mergedFile);
            doMerge(list, out);
        } catch (IOException | DocumentException e) {
        	logger.error("Exception", e);
        }

		return mergedFile;
	}
	
	private void doMerge(List<StreamPwdContainer> list, OutputStream outputStream) throws DocumentException, IOException {
		HashMap<Integer, String> rotationFromPages = BAG.getRotationFromPages();
		Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();
        PdfContentByte cb = writer.getDirectContent();
        
        int z = 0;
        for (StreamPwdContainer boom : list) {
        	
        	InputStream in = boom.getIn();
        	PdfReader reader = null;
        	
        	if (!boom.getPwd().equalsIgnoreCase("")) {
        		reader = new PdfReader(in, boom.getPwd().getBytes());
        	} else {
        		reader = new PdfReader(in);
        	}
            
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            	z++;
            	int rotation = reader.getPageRotation(i);
            	
                //set size
            	Rectangle pageSize_ = reader.getPageSize(i);
            	Rectangle pageSize = null;
            	
            	if (rotation==270 || rotation==90) {	
            		pageSize = new Rectangle(pageSize_.getHeight(), pageSize_.getWidth());
            	} else {
            		pageSize = pageSize_;
            	}
            	
            	document.setPageSize(pageSize);
            	writer.setCropBoxSize(pageSize);
            	
            	document.newPage();
                
                // import the page from source pdf
                PdfImportedPage page = writer.getImportedPage(reader, i);
                
                // add the page to the destination pdf
                if (rotation == 270) {
                	cb.addTemplate(page, 0, 1.0f, -1.0f, 0, reader.getPageSizeWithRotation(i).getWidth(), 0);
                	rotationFromPages.put(z, ""+rotation);
                }
                else if (rotation == 180) {
                	cb.addTemplate(page, -1f, 0, 0, -1f, 0, 0);
                	rotationFromPages.put(z, ""+rotation);
                }		 
                else if (rotation == 90) {
                	cb.addTemplate(page, 0, -1f, 1f, 0, 0, reader.getPageSizeWithRotation(i).getHeight());
                	rotationFromPages.put(z, ""+rotation);
                } else {
                	cb.addTemplate(page, 1f, 0, 0, 1f, 0, 0);
                }   
            }
            in.close();
        }
        outputStream.flush();
        document.close();
        outputStream.close();
    }
	
	public class StreamPwdContainer {
		public InputStream in;
		public String pwd;
		
		public InputStream getIn() {
			return in;
		}
		
		public void setIn(InputStream in) {
			this.in = in;
		}
		
		public String getPwd() {
			return pwd;
		}
		
		public void setPwd(String pwd) {
			this.pwd = pwd;
		}	
	}
	
}
