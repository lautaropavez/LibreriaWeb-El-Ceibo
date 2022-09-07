/**
 *
 * @author Toshiba
 */
//package com.mza.Libreria.pdf;

//import com.lowagie.text.Document;
//import com.lowagie.text.Element;
//import com.lowagie.text.PageSize;
//import com.lowagie.text.Phrase;
//import com.lowagie.text.pdf.PdfPCell;
//import com.lowagie.text.pdf.PdfPTable;
//import com.lowagie.text.pdf.PdfWriter;
//import com.mza.Libreria.entidades.Libro;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.view.document.AbstractPdfView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.awt.*;
//import java.util.List;
//import java.util.Map;


// ============== Data de donde sacar para hacer que funcione el pdf =======================

//  github donde lo sacamos(ya lo descargamos): https://github.com/acamus79/BibliotecaWeb/blob/main/BibliotecaWeb%20v0.02/pom.xml
//  clase para crearlo(igual en la que estamos ahora): https://github.com/acamus79/BibliotecaWeb/blob/main/BibliotecaWeb%20v0.02/src/main/java/com/mza/biblioteca/util/ListaLibrosPDF.java

//  DATA DE DONDE PUDO HABER SACADO LA INFO DE PDF (REPO DE GITHUB DONDE ESTÁ SUBIDO EL PROYECTO)
//  https://github.com/LibrePDF/OpenPDF
//  https://en.wikipedia.org/wiki/GNU_Lesser_General_Public_License
//  https://github.com/LibrePDF/OpenPDF/tree/master/pdf-toolbox/src/test/java/com/lowagie/examples
//  https://librepdf.github.io/OpenPDF/docs-1-3-17/
//  https://github.com/LibrePDF/OpenPDF/wiki/Tutorial

//  INFO EXTRA
//  https://www.baeldung.com/java-pdf-creation  vienen las dos juntas  https://itextpdf.com/blog/technical-notes/how-do-i-make-sure-my-software-complies-agpl-how-can-i-use-itext-free
//  https://www.geeksforgeeks.org/formatting-the-text-in-a-pdf-using-java/
//  https://picodotdev.github.io/blog-bitix/2019/08/ejemplo-sencillo-de-como-crear-un-documento-pdf-con-java-y-pdfbox/
//  https://www.tutorialspoint.com/pdfbox/pdfbox_inserting_image.htm






//@Component("listalibros")
//public class LibrosPDF extends AbstractPdfView{
//
//    @Override
//    protected void buildPdfDocument(Map<String, Object> model,
//                                    Document document,
//                                    PdfWriter writer,
//                                    HttpServletRequest request,
//                                    HttpServletResponse response) throws Exception {
//
//        List<Libro> listaLibros = (List<Libro>) model.get("libros");
//        float[] anchos = {8f, 60f, 50f, 50f, 10f, 10f, 10f, 10f};
//
//        //creo una tabla para el titulo
//        final PdfPTable tablaTitulo = new PdfPTable(1);
//        //creo una Celda titulo y le doy formato
//        PdfPCell titulo = new PdfPCell(new Phrase("LISTA DE LIBROS REGISTRADOS"));
//        titulo.setBorder(0);
//        titulo.setBackgroundColor(new Color(100, 161, 157));
//        titulo.setHorizontalAlignment(Element.ALIGN_CENTER);
//        titulo.setVerticalAlignment(Element.ALIGN_CENTER);
//        titulo.setPadding(5);
//        //a la tabla Titulo le agrego la Celda titulo
//        tablaTitulo.addCell(titulo);
//        tablaTitulo.setSpacingAfter(15);//para agregar espacio luego de esta tabla
//
//        //Creo un objeto de PdfPTable de 8 columnas para los libros
//        final PdfPTable tablaLibros = new PdfPTable(8);
//        tablaLibros.setSplitLate(true);
//        tablaLibros.setWidths(anchos);
//
//        //Creo una tablaigual de 8 columnas para los nombres de cada columna
//        final PdfPTable tablaNomb = new PdfPTable(8);
//        tablaNomb.setWidths(anchos);
//        tablaNomb.addCell(new PdfPCell(new Phrase("Nro.")));
//        tablaNomb.addCell(new PdfPCell(new Phrase("Titulo")));
//        tablaNomb.addCell(new PdfPCell(new Phrase("Autor")));
//        tablaNomb.addCell(new PdfPCell(new Phrase("Editorial")));
//        tablaNomb.addCell(new PdfPCell(new Phrase("Ej.")));
//        tablaNomb.addCell(new PdfPCell(new Phrase("Pr.")));
//        tablaNomb.addCell(new PdfPCell(new Phrase("Dis.")));
//        tablaNomb.addCell(new PdfPCell(new Phrase("Est.")));
//
//        //dando el tamaño al Documento y poniendolo en horizontal
//        document.setPageSize(PageSize.A4.rotate());
//        document.setMargins(-25, -30, 20, 10);
//        //abro el documento 
//        document.open();
//
//
//        //PdfPCell nro = new 
//        
//        
//        
//        
//        /*
//        Para rellenar la tablaLibros:
//        Utilizo un For comun ya que necesito un indice para contar cuantos libros
//        tiene la lista, ya que mi ID es de tipo String y no un autoincremental
//         */
//        for (int i = 0; i < listaLibros.size(); i++) {
//            tablaLibros.addCell(String.valueOf(i + 1));//asi obtengo un contador de libros
//            tablaLibros.addCell(listaLibros.get(i).getTitulo());
//            tablaLibros.addCell(listaLibros.get(i).getAutor().getNombre());
//            tablaLibros.addCell(listaLibros.get(i).getEditorial().getNombre());
//            tablaLibros.addCell(listaLibros.get(i).getNroejemplares().toString());
//            tablaLibros.addCell(listaLibros.get(i).getEjemplaresPrestados().toString());
//            tablaLibros.addCell(listaLibros.get(i).getEjemplaresRestantes().toString());
//            tablaLibros.addCell(listaLibros.get(i).getAlta().toString());
//        }
//
//        document.addTitle("LISTA DE LIBROS REGISTRADOS");
//        document.add(tablaTitulo);
//        document.add(tablaNomb);
//        document.add(tablaLibros);
//        document.close();
//    
//}