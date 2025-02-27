package cart.dao;

import cart.dto.entity.ProductEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class ProductDao {
    private final JdbcTemplate jdbcTemplate;

    public ProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ProductEntity save(final ProductEntity product) {
        final String sql = "INSERT INTO products(name, image, price) VALUES (?, ?, ?)";

        final GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            final PreparedStatement preparedStatement = con.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getImage());
            preparedStatement.setInt(3, product.getPrice());

            return preparedStatement;
        }, generatedKeyHolder);

        product.setId(generatedKeyHolder.getKey().longValue());
        return product;
    }

    public List<ProductEntity> findAll() {
        final String sql = "SELECT id, name, image, price FROM products";

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new ProductEntity(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getInt(4)));
    }

    public ProductEntity update(final ProductEntity product) {
        final String sql = "UPDATE products SET id=?, name=?, image=?, price=? WHERE id=?";

        jdbcTemplate.update(sql, product.getId(), product.getName(), product.getImage(), product.getPrice(), product.getId());
        return product;
    }

    public void delete(final Long id) {
        final String sql = "DELETE FROM products WHERE id=?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existById(final Long id) {
        final String sql = "SELECT count(*) FROM products WHERE id=?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{id}, Integer.class);
        return count > 0;
    }
}
