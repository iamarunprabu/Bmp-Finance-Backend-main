package com.security.JWT.ServiceImpl;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class JasperReportService {

	public byte[] generatePDF(String reportName, List<?> data, Map<String, Object> parameters) throws Exception {
		InputStream inputStream;
		// during development it's handy to load the JRXML directly from the project
		// directory so changes take effect without rebuilding the JAR.  If the file
		// exists under src/main/resources, prefer that; otherwise fall back to the
		// classpath resource (e.g. when running from a packaged JAR).
		java.nio.file.Path devPath = java.nio.file.Paths.get("src","main","resources","reports", reportName + ".jrxml");
		if (java.nio.file.Files.exists(devPath)) {
			inputStream = java.nio.file.Files.newInputStream(devPath);
			System.out.println("Loading JRXML from file system: " + devPath.toAbsolutePath());
		} else {
			inputStream = new ClassPathResource("reports/" + reportName + ".jrxml").getInputStream();
			System.out.println("Loading JRXML from classpath resource");
		}

		System.out.println("DEBUG: Data source size = " + data.size() + ", Parameters = " + parameters.keySet());
		
		try {
			JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
			System.out.println("DEBUG: JasperReport compiled successfully");

			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);
			System.out.println("DEBUG: DataSource created with " + data.size() + " records");

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
					parameters, dataSource);
			System.out.println("DEBUG: JasperPrint filled successfully. Page count: " + jasperPrint.getPages().size());

			byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
			System.out.println("DEBUG: PDF exported successfully. Size: " + pdf.length + " bytes");
			
			return pdf;
		} catch (Exception e) {
			System.err.println("ERROR in PDF generation: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}
}
