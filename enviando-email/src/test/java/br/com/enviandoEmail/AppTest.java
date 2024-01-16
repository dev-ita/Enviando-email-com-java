package br.com.enviandoEmail;

import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	@org.junit.Test
	public void testeEmail() {
		StringBuilder stringBuilderTextoEmail = new StringBuilder();
		stringBuilderTextoEmail.append("Olá, <br/><br/>");
		stringBuilderTextoEmail.append("<h2>Você está recebendo o acesso ao curso de Java</h2> <br/><br/>");
		stringBuilderTextoEmail.append("para ter acesso clique no botão abaixo.<br/><br/>");
		stringBuilderTextoEmail.append("<br>Login:<br> italo.ods@hotmail.com<br/>");
		stringBuilderTextoEmail.append("<br>Senha:<br> Kdoiek_2kSo1lamN<br/><br/>");
		stringBuilderTextoEmail.append(
				"<a target=\"_blank\" href=\"https://www.google.com\"><button style=\"padding: 10px 20px; background-color: #3498db; color: #fff; border: none; border-radius: 5px; cursor: pointer;\">Acessar Portal</button></a><br/><br/>");
		stringBuilderTextoEmail.append("<span style=\"font-size: 8px\">Ass.: Ítalo Oliveira</span>");

		try {
			ObjetoEnviaEmail email = new ObjetoEnviaEmail("italobluestacks@gmail.com, italo.ods@hotmail.com",
					"ítalo Oliveira Dev", "Java em 2024", stringBuilderTextoEmail.toString());
			email.enviarEmailAnexo(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}