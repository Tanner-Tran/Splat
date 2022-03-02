package com.splat.main;

import com.codahale.metrics.health.HealthCheckRegistry;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.splat.controller.HealthCheckController;
import com.splat.controller.RestaurantController;
import com.splat.controller.ReviewsController;
import com.splat.data.DAO.AccountDAO;
import com.splat.controller.AccountsController;
import com.splat.configuration.ServiceConfiguration;
import com.splat.data.DAO.RestaurantDAO;
import com.splat.data.DAO.ReviewDAO;
import com.splat.service.AccountsService;
import com.splat.service.ReviewsService;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.flyway.FlywayBundle;
import io.dropwizard.flyway.FlywayFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.jdbi.v3.core.Jdbi;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.io.FileInputStream;
import java.util.EnumSet;

public class ServiceApplication extends Application<ServiceConfiguration> {
    public static void main(String[] args) throws Exception {
        new ServiceApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<ServiceConfiguration> bootstrap) {
        // Register the flyway commands
        bootstrap.addBundle(new FlywayBundle<ServiceConfiguration>(){
            @Override
            public DataSourceFactory getDataSourceFactory(ServiceConfiguration config) {
                return config.getDatabase();
            }

            @Override
            public FlywayFactory getFlywayFactory(ServiceConfiguration config) {
                return config.getFlyway();
            }
        });
    }

    @Override
    public void run(ServiceConfiguration config, Environment env) throws Exception {

        try (FileInputStream serviceAccount =
                     new FileInputStream("firebase.json")) {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
        }

        // Get a database handle
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(env, config.getDatabase(), "database");

        // CORS
        final FilterRegistration.Dynamic cors =
                env.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        // Build and register resources
        AccountDAO accountDAO = new AccountDAO(jdbi);
        AccountsService accountsService = new AccountsService(accountDAO);

        ReviewDAO reviewDAO = new ReviewDAO(jdbi);
        RestaurantDAO restaurantDAO = new RestaurantDAO(jdbi);
        ReviewsService reviewsService = new ReviewsService(reviewDAO, restaurantDAO);

        HealthCheckRegistry registry = new HealthCheckRegistry();

        env.jersey().register(new AccountsController(accountsService));
        env.jersey().register(new ReviewsController(reviewsService));
        env.jersey().register(new RestaurantController(restaurantDAO));
        env.jersey().register(new HealthCheckController(registry));
    }
}
