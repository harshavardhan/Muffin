package org.muffin.muffin.daos;

import java.util.Optional;

import org.muffin.muffin.beans.Movie;
import org.muffin.muffin.beans.Review;

import lombok.NonNull;

public interface ReviewDAO {

    public boolean create(final int movieId, final int muffId, final float rating, @NonNull final String text);

    public Optional<Review> get(final int movieId, final int muffId);

}
