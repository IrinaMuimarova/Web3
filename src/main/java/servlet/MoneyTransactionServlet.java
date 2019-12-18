package servlet;

import com.mysql.cj.xdevapi.Client;
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

public class MoneyTransactionServlet extends HttpServlet {

    BankClientService bankClientService = new BankClientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(PageGenerator.getInstance().getPage("moneyTransactionPage.html", new HashMap<>()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nameSender = req.getParameter("senderName");
        String passwordSender = req.getParameter("senderPass");
        Long count = Long.parseLong(req.getParameter("count"));
        String nameTo = req.getParameter("nameTo");
        BankClient client = null;
        try {
            client = bankClientService.getClientByName(nameSender);
        } catch (DBException e) {
        }
        boolean result = false;
        if (client != null && client.getPassword().equals(passwordSender)) {
            result = bankClientService.sendMoneyToClient(client, nameTo, count);
        }
        String message = result ? "The transaction was successful" : "transaction rejected";
        Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", map));
    }
}
