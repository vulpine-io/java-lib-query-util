package io.vulpine.lib.query.util.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Map;
import javax.sql.DataSource;

import io.vulpine.lib.query.util.ConnectionProvider;
import io.vulpine.lib.query.util.MapReadQuery;
import io.vulpine.lib.query.util.ReadResult;

public abstract class PreparedMapReadQuery <
  K, V,
  R extends ReadResult < Map < K, V >, ? extends MapReadQuery < ?, ?, ?, ? > > >
extends MapReadQueryImpl < K, V, R, PreparedStatement >
implements MapReadQuery < K, V, R, PreparedStatement >
{
  public PreparedMapReadQuery(String sql, ConnectionProvider provider) {
    super(sql, provider);
  }

  public PreparedMapReadQuery(String sql, DataSource ds) {
    super(sql, ds);
  }

  public PreparedMapReadQuery(String sql, Connection cn) {
    super(sql, cn);
  }

  @Override
  protected R executeStatement(PreparedStatement stmt) throws Exception {
    try (var rs = stmt.executeQuery()) {
      return toResult(stmt, parseResult(rs));
    }
  }

  @Override
  protected PreparedStatement getStatement(Connection cn) throws Exception {
    var out = cn.prepareStatement(getSql());
    prepareStatement(out);
    return out;
  }

  protected abstract void prepareStatement(PreparedStatement ps)
  throws Exception;
}
