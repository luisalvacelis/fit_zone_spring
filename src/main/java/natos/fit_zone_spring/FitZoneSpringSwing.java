package natos.fit_zone_spring;

import com.formdev.flatlaf.FlatDarculaLaf;
import natos.fit_zone_spring.view.FitZoneView;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;

@SpringBootApplication
public class FitZoneSpringSwing {
    public static void main(String[] args) {
        FlatDarculaLaf.setup();
        ConfigurableApplicationContext contextSpring =
                new SpringApplicationBuilder(FitZoneSpringSwing.class)
                        .headless(false)
                        .web(WebApplicationType.NONE)
                        .run(args);
        SwingUtilities.invokeLater(
                () -> {
                    FitZoneView fitZoneView = contextSpring.getBean(FitZoneView.class);
                    fitZoneView.setVisible(true);
                });
    }
}
