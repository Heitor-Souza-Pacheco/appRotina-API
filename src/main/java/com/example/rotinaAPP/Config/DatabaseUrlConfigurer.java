package com.example.rotinaAPP.Config;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * Torna a aplicação resiliente ao formato de URL de banco fornecido por
 * provedores como o Railway/Heroku.
 *
 * <p>Esses provedores expõem a variável {@code DATABASE_URL} no formato
 * {@code postgresql://usuario:senha@host:porta/banco}, que o Spring/HikariCP
 * NÃO consegue usar diretamente (ele espera uma URL JDBC, ex.:
 * {@code jdbc:postgresql://host:porta/banco}).</p>
 *
 * <p>Este utilitário detecta a URL no formato "postgres://" e a converte,
 * definindo {@code spring.datasource.*} como <em>system properties</em> antes
 * da inicialização do contexto Spring. System properties têm precedência sobre
 * o {@code application.properties}, então sobrescrevem o placeholder padrão.</p>
 *
 * <p>Se {@code DATABASE_URL} já estiver em formato JDBC (ou não existir),
 * nada é alterado — o comportamento continua o mesmo de antes.</p>
 */
public final class DatabaseUrlConfigurer {

    private DatabaseUrlConfigurer() {
    }

    public static void apply() {
        String databaseUrl = System.getenv("DATABASE_URL");
        if (databaseUrl == null || databaseUrl.isBlank()) {
            return;
        }

        databaseUrl = databaseUrl.trim();

        // Já está no formato esperado pelo Spring — não faz nada.
        if (databaseUrl.startsWith("jdbc:")) {
            return;
        }

        if (!databaseUrl.startsWith("postgres://")
                && !databaseUrl.startsWith("postgresql://")) {
            return;
        }

        try {
            URI uri = new URI(databaseUrl);

            String host = uri.getHost();
            int port = uri.getPort() == -1 ? 5432 : uri.getPort();
            String path = uri.getPath() == null ? "" : uri.getPath();

            StringBuilder jdbc = new StringBuilder("jdbc:postgresql://")
                    .append(host)
                    .append(':')
                    .append(port)
                    .append(path);

            // Preserva query string (ex.: ?sslmode=require) se houver.
            if (uri.getQuery() != null && !uri.getQuery().isBlank()) {
                jdbc.append('?').append(uri.getQuery());
            }

            System.setProperty("spring.datasource.url", jdbc.toString());

            String userInfo = uri.getUserInfo();
            if (userInfo != null && !userInfo.isBlank()) {
                String[] partes = userInfo.split(":", 2);
                String usuario = URLDecoder.decode(partes[0], StandardCharsets.UTF_8);
                System.setProperty("spring.datasource.username", usuario);
                if (partes.length > 1) {
                    String senha = URLDecoder.decode(partes[1], StandardCharsets.UTF_8);
                    System.setProperty("spring.datasource.password", senha);
                }
            }

            System.out.println(
                    "[DatabaseUrlConfigurer] DATABASE_URL convertida para JDBC: "
                            + jdbc);
        } catch (Exception e) {
            System.err.println(
                    "[DatabaseUrlConfigurer] Falha ao converter DATABASE_URL: "
                            + e.getMessage());
        }
    }
}
