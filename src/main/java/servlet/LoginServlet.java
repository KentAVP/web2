package servlet;

import model.User;
import service.UserService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        String login = req.getParameter("email");
        String pass = req.getParameter("password");

        if (login == null || pass == null) {
            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().print("null");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } else {
            Set<Map.Entry<Long, User>> entrySet = UserService.getInstance().db().entrySet();
            Long x = 0l;

            User desiredObject = new User(login, pass);//что хотим найти
            for (Map.Entry<Long, User> pair : entrySet) {
                if (desiredObject.equals(pair.getValue())) {
                    x = pair.getKey();// нашли наше значение и возвращаем  ключ
                }
            }
            User nj = new User(x, login, pass);
            boolean zz = nj.getEmail().equals(desiredObject.getEmail()) && nj.getPassword().equals(desiredObject.getPassword()) && x > 0l? true : false;


            if (!UserService.getInstance().db().containsValue(nj) && !zz) { //делать проверку на не существующих юзеров
                resp.setContentType("text/html;charset=utf-8");
                resp.getWriter().print("not exist");
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            } else {
                if(zz){
                    UserService.getInstance().authUser(nj);
                    resp.getWriter().print("Ok");
                    resp.setStatus(HttpServletResponse.SC_OK);
                    return;
                }else{
                    resp.setContentType("text/html;charset=utf-8");
                    resp.getWriter().print("null");
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

            }
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Map<String, Object> pageVariables = createPageVariablesMap(req);

        resp.getWriter().print(PageGenerator.getInstance().getPage("authPage.html", pageVariables));
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private static Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("method", request.getMethod());
        pageVariables.put("URL", request.getRequestURL().toString());
        pageVariables.put("pathInfo", request.getPathInfo());
        pageVariables.put("sessionId", request.getSession().getId());
        pageVariables.put("parameters", request.getParameterMap().toString());
        return pageVariables;
    }
}
