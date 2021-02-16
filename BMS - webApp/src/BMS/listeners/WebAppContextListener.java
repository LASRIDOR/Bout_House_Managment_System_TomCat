package BMS.listeners;

import BMS.constants.Constants;
import BMS.managment.CEO.BoutHouseManager;
import BMS.managment.reservationManager.InvalidActionException;
import BMS.server.BoutHouseDataType;
import BMS.xml.exceptions.ExtensionException;
import BMS.xml.xmlManager.XmlManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

@WebListener("WebApp Context Listener")
public class WebAppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext context = servletContextEvent.getServletContext();
        BoutHouseManager manager = new BoutHouseManager();

        try {
            manager.loadDataBaseXml(BoutHouseDataType.MEMBERS);
            manager.loadDataBaseXml(BoutHouseDataType.TIME_WINDOW);
            manager.loadDataBaseXml(BoutHouseDataType.BOATS);
        } catch (FileNotFoundException | ExtensionException | JAXBException | InvalidActionException e) {
            System.out.println(e.getMessage());
        }

        context.setAttribute(Constants.BOUT_HOUSE_MANAGER, manager);
        context.setAttribute(Constants.XML_MANAGER, new XmlManager());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        // TODO maybe log every one out
    }
}
