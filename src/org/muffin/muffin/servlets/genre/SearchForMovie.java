package org.muffin.muffin.servlets.genre;

import org.muffin.muffin.beans.*;
import org.muffin.muffin.daoimplementations.GenreDAOImpl;
import org.muffin.muffin.daos.GenreDAO;
import org.muffin.muffin.responses.ResponseWrapper;
import org.muffin.muffin.servlets.MovieOwnerEnsuredSessionServlet;

import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * doGetWithSession:  tries to ad a new genre to the movie with given params, if success returns created obj, else returns error
 * doPostWithSession: same as GET
 */
@WebServlet("/genre/searchformovie")
public class SearchForMovie extends MovieOwnerEnsuredSessionServlet {
    private GenreDAO genreDAO = new GenreDAOImpl();

    @Override
    protected void doGetWithSession(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
        String pattern = request.getParameter("pattern");
        int offset = Integer.parseInt(request.getParameter("offset"));
        int movieId = Integer.parseInt(request.getParameter("movieId"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        List<Genre> genres = genreDAO.searchGenresForMovie(pattern, movieId, offset, limit);
        PrintWriter out = response.getWriter();
        out.println(new GsonBuilder().create().toJson(ResponseWrapper.get(genres, ResponseWrapper.ARRAY_RESPONSE)));
        out.close();
    }

    @Override
    protected void doPostWithSession(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
        doGetWithSession(request, response, session);
    }
}

