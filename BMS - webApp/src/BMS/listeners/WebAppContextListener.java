package BMS.listeners;

import BMS.constants.Constants;
import BMS.managment.CEO.BoutHouseManager;
import BMS.managment.reservationManager.InvalidActionException;
import BMS.server.BoutHouseDataType;
import BMS.xml.exceptions.ExtensionException;
import BMS.xml.xmlManager.XmlManager;
import chat.ChatManager;
import notification.NotificationManagerForAllMembers;
import notification.NotificationManagerForMember;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

// TODO kick xml manager from servlet
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
            System.out.println("Database Was uploaded Successfully");
        } catch (FileNotFoundException | ExtensionException | JAXBException | InvalidActionException e) {
            System.out.println(e.getMessage());
        }

        context.setAttribute(Constants.BOUT_HOUSE_MANAGER, manager);
        context.setAttribute(Constants.XML_MANAGER, new XmlManager());
        context.setAttribute(Constants.CHAT_MANAGER_ATTRIBUTE_NAME, new ChatManager());
        context.setAttribute(Constants.ALL_MEMBERS_NOTIFICATION_ATTRIBUTE_NAME, new NotificationManagerForAllMembers());
        context.setAttribute(Constants.MEMBER_NOTIFICATION_ATTRIBUTE_NAME, new NotificationManagerForMember());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        // TODO maybe log every one out
    }
}
