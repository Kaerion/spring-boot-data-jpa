package com.nombreempresa.springboot.app.view.xlsx;

import java.util.Locale;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.nombreempresa.springboot.app.models.entity.Factura;
import com.nombreempresa.springboot.app.models.entity.ItemFactura;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component("factura/ver.xlsx")
public class FacturaXlsxView extends AbstractXlsxView {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private LocaleResolver localeResolver;

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Locale locale = localeResolver.resolveLocale(request);
		Factura factura = (Factura) model.get("factura");

		Sheet sheet = workbook.createSheet();

		Row row = sheet.createRow(0);
		Cell cell = row.createCell(0);

		cell.setCellValue(messageSource.getMessage("text.cliente.title", null, locale));
		row = sheet.createRow(1);
		cell = row.createCell(0);
		cell.setCellValue(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());

		row = sheet.createRow(2);
		cell = row.createCell(0);
		cell.setCellValue(factura.getCliente().getEmail());

		sheet.createRow(4).createCell(0).setCellValue(messageSource.getMessage("text.factura.title", null, locale));
		sheet.createRow(5).createCell(0)
				.setCellValue(messageSource.getMessage("pdf.factura.id", null, locale) + " " + factura.getId());
		sheet.createRow(6).createCell(0).setCellValue(
				messageSource.getMessage("pdf.factura.descripcion", null, locale) + " " + factura.getDescripcion());
		sheet.createRow(7).createCell(0).setCellValue(
				messageSource.getMessage("pdf.factura.fecha", null, locale) + " " + factura.getCreateAt());

		Row header = sheet.createRow(9);
		header.createCell(0).setCellValue(messageSource.getMessage("pdf.factura.producto", null, locale));
		header.createCell(1).setCellValue(messageSource.getMessage("pdf.factura.precio", null, locale));
		header.createCell(2).setCellValue(messageSource.getMessage("pdf.factura.cantidad", null, locale));
		header.createCell(3).setCellValue(messageSource.getMessage("pdf.factura.total", null, locale));

		int rownum = 10;
		for (ItemFactura item : factura.getItems()) {
			Row itemRow = sheet.createRow(rownum++);
			itemRow.createCell(0).setCellValue(item.getProducto().getNombre());
			itemRow.createCell(1).setCellValue(item.getProducto().getPrecio());
			itemRow.createCell(2).setCellValue(item.getCantidad());
			itemRow.createCell(3).setCellValue(item.calcularImporte());
		}

		Row filaTotal = sheet.createRow(rownum);
		filaTotal.createCell(2).setCellValue(messageSource.getMessage("pdf.factura.total", null, locale));
		filaTotal.createCell(3).setCellValue(factura.getTotal());

	}

}
