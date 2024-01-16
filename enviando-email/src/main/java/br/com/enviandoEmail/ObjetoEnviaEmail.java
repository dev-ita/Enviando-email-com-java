package br.com.enviandoEmail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class ObjetoEnviaEmail {

	private String username = "italojavamailtest@gmail.com";
	private String password = "kzva btsu plas xxdb";
	private String listaDestinatarios = "";
	private String nomeRemetente = "";
	private String assuntoEmail = "";
	private String mensagemEmail = "";
	private Properties properties = new Properties();

	public ObjetoEnviaEmail(String listaDestinatarios, String nomeRemetente, String assuntoEmail,
			String mensagemEmail) {
		this.listaDestinatarios = listaDestinatarios;
		this.nomeRemetente = nomeRemetente;
		this.assuntoEmail = assuntoEmail;
		this.mensagemEmail = mensagemEmail;

		// configuração do servidor
		properties.put("mail.smtp.ssl.trust", "*"); // autenticar com segurança SSL
		properties.put("mail.smtp.auth", "true"); // autorização
		properties.put("mail.smtp.starttls", "true"); // autenticação
		properties.put("mail.smtp.host", "smtp.gmail.com"); // servidor gmail
		properties.put("mail.smtp.port", "465"); // porta do servidor
		properties.put("mail.smtp.socketFactory.port", "465"); // expecifíca a porta a ser conectada pelo socket
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); // classe socket de conexão
																							// ao SMTP
	}

	public void enviarEmail(boolean envioHtml) throws UnsupportedEncodingException, MessagingException {
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		Address[] toUser = InternetAddress.parse(listaDestinatarios);

		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(username, nomeRemetente)); // quem está enviando
		message.setRecipients(Message.RecipientType.TO, toUser); // email de destino
		message.setSubject(assuntoEmail);

		if (envioHtml) {
			message.setContent(mensagemEmail, "text/html; charset=utf-8");
		} else {
			message.setText(mensagemEmail);
		}

		Transport.send(message);
	}

	public void enviarEmailAnexo(boolean envioHtml) throws MessagingException, IOException, DocumentException {
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		Address[] toUser = InternetAddress.parse(listaDestinatarios);

		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(username, nomeRemetente)); // quem está enviando
		message.setRecipients(Message.RecipientType.TO, toUser); // email de destino
		message.setSubject(assuntoEmail);

		MimeBodyPart corpoEmail = new MimeBodyPart();

		if (envioHtml) {
			corpoEmail.setContent(mensagemEmail, "text/html; charset=utf-8");
		} else {
			corpoEmail.setText(mensagemEmail);
		}

		List<FileInputStream> arquivos = new ArrayList<FileInputStream>();
		arquivos.add(simuladorDePDF());
		arquivos.add(simuladorDePDF());
		arquivos.add(simuladorDePDF());
		arquivos.add(simuladorDePDF());

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(corpoEmail);

		int index = 0;
		for (FileInputStream fileInputStream : arquivos) {
			MimeBodyPart anexoEmail = new MimeBodyPart();

			// onde é passado o simulador de pdf, você passa seu arquivo gravado no banco de
			// dados.
			anexoEmail.setDataHandler(new DataHandler(new ByteArrayDataSource(fileInputStream, "application/pdf")));
			anexoEmail.setFileName("anexoEmail" + index + ".pdf");

			multipart.addBodyPart(anexoEmail);
			index++;
		}

		message.setContent(multipart);

		Transport.send(message);
	}

	private FileInputStream simuladorDePDF() throws IOException, DocumentException {
		Document document = new Document();
		File file = new File("anexo.pdf");
		file.createNewFile();
		PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();
		document.add(new Paragraph("Conteúdo do PDF anexo com Java Mail, esse texto é do PDF"));
		document.close();

		return new FileInputStream(file);
	}
}
