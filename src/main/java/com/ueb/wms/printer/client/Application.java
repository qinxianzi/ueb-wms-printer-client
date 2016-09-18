package com.ueb.wms.printer.client;

import java.awt.Font;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.ueb.wms.printer.client.view.LoginView;

@SpringBootApplication(scanBasePackages = "com.ueb.wms.printer.client")
public class Application implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	@Autowired
	private LoginView loginView;

	public static void main(String[] args) {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(Application.class);
		builder.headless(false).web(false).run(args);
	}

	public void run(String... args) throws Exception {
		logger.info("路径是：{}", Application.class.getResource("/").getFile());
		logger.info("路径是22：{}", Application.class.getResource("/").getPath());
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Application.this.setFont();
				// Application.this.mainView.displayUI();
				Application.this.loginView.displayUI();
			}
		});
	}

	private void setFont() {
		Font f = new Font("微软雅黑", Font.PLAIN, 12);
		UIManager.put("Label.font", f);
		UIManager.put("ComboBox.font", f);
		UIManager.put("Button.font", f);
		UIManager.put("TabbedPane.font", f);
		UIManager.put("RadioButton.font", f);
		UIManager.put("TitledBorder.font", f);
	}
}
