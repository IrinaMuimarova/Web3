package servlet;

import exception.DBException;
import model.BankClient;
import service.BankClientService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegistrationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(PageGenerator.getInstance().getPage("registrationPage.html", new HashMap<>()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean addClient = false;
        addClient = new BankClientService().addClient(new BankClient(req.getParameter("name"),
               req.getParameter("password"),
               Long.parseLong(req.getParameter("money"))));

        String message = addClient ? "Add client successful" : "Client not add";

        resp.setContentType("text/html;charset=utf-8");
        Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", map));
    }
}
