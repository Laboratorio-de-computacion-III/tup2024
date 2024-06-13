package ar.edu.utn.frbb.tup;

import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNotSupportedException;
import ar.edu.utn.frbb.tup.presentation.input.MenuInputProcessor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

class Application {

    public static void main(String args[]) throws TipoCuentaAlreadyExistsException, TipoCuentaNotSupportedException {

        ConfigurableApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(ApplicationConfig.class);

        MenuInputProcessor processor = applicationContext.getBean(MenuInputProcessor.class);
        processor.renderMenu();
        applicationContext.close();
    }


}
