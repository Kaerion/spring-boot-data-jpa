package com.nombreempresa.springboot.app.view.pdf;

import java.awt.Color;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.nombreempresa.springboot.app.models.entity.Factura;
import com.nombreempresa.springboot.app.models.entity.ItemFactura;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component("factura/ver")
public class FacturaPdfView extends AbstractPdfView {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private LocaleResolver localeResolver;

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		Locale locale = localeResolver.resolveLocale(request);
		Factura factura = (Factura) model.get("factura");

		PdfPTable table = new PdfPTable(1);
		table.setSpacingAfter(20);

		PdfPCell cell = null;
		cell = new PdfPCell(new Phrase(messageSource.getMessage("text.cliente.title", null, locale)));
		cell.setBackgroundColor(new Color(184, 218, 255));
		cell.setPadding(8f);
		table.addCell(cell);

		table.addCell(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
		table.addCell(factura.getCliente().getEmail());

		PdfPTable table2 = new PdfPTable(1);
		table2.setSpacingAfter(20);

		cell = new PdfPCell(new Phrase(messageSource.getMessage("text.factura.title", null, locale)));
		cell.setBackgroundColor(new Color(195, 230, 203));
		cell.setPadding(8f);
		table2.addCell(cell);

		table2.addCell(messageSource.getMessage("pdf.factura.id", null, locale) + " " + factura.getId());
		table2.addCell(
				messageSource.getMessage("pdf.factura.descripcion", null, locale) + " " + factura.getDescripcion());
		table2.addCell(messageSource.getMessage("pdf.factura.fecha", null, locale) + " " + factura.getCreateAt());

		PdfPTable table3 = new PdfPTable(4);
		table3.setWidths(new float[] { 2.5f, 1, 1, 1 });
		table3.addCell(messageSource.getMessage("pdf.factura.producto", null, locale));
		table3.addCell(messageSource.getMessage("pdf.factura.precio", null, locale));
		table3.addCell(messageSource.getMessage("pdf.factura.cantidad", null, locale));
		table3.addCell(messageSource.getMessage("pdf.factura.total", null, locale));

		for (ItemFactura item : factura.getItems()) {
			table3.addCell(item.getProducto().getNombre());
			table3.addCell(item.getProducto().getPrecio().toString());

			cell = new PdfPCell(new Phrase(item.getCantidad().toString()));
			cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			table3.addCell(cell);
			table3.addCell(item.calcularImporte().toString());
		}

		cell = new PdfPCell(new Phrase(messageSource.getMessage("pdf.factura.total", null, locale) + ": "));
		cell.setColspan(3);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		table3.addCell(cell);
		table3.addCell(factura.getTotal().toString());

		document.add(table);
		document.add(table2);
		document.add(table3);
	}

}
