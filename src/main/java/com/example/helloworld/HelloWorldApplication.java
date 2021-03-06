package com.example.helloworld;

import com.example.helloworld.auth.ExampleAuthorizer;
import com.example.helloworld.core.*;
import com.example.helloworld.db.*;
import com.example.helloworld.resources.*;
import custom.resources.gojs.GoJsResource;
import custom.resources.ProcessResource;
import io.dropwizard.auth.AuthValueFactoryProvider;
import com.example.helloworld.auth.ExampleAuthenticator;
import com.example.helloworld.cli.RenderCommand;
import custom.dao.ProcessDAO;
import custom.dao.TaskConnectionDAO;
import custom.dao.TaskDAO;
import custom.domain.Process;
import com.example.helloworld.filter.DateRequiredFeature;
import com.example.helloworld.health.TemplateHealthCheck;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import custom.domain.ProcessTask;
import custom.domain.Task;
import custom.domain.TaskConnection;
import custom.resources.TaskConnectionResource;
import custom.resources.TaskResource;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import java.util.Map;

public class HelloWorldApplication extends Application<HelloWorldConfiguration> {
    public static void main(String[] args) throws Exception {
        new HelloWorldApplication().run(args);
    }

    private final HibernateBundle<HelloWorldConfiguration> hibernateBundle =
            new HibernateBundle<HelloWorldConfiguration>(Person.class, Process.class, Task.class,
                    TaskConnection.class, ProcessTask.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(HelloWorldConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<HelloWorldConfiguration> bootstrap) {
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );

        bootstrap.addCommand(new RenderCommand());
        bootstrap.addBundle(new AssetsBundle());
        bootstrap.addBundle(new MigrationsBundle<HelloWorldConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(HelloWorldConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
        bootstrap.addBundle(hibernateBundle);
        bootstrap.addBundle(new ViewBundle<HelloWorldConfiguration>() {
            @Override
            public Map<String, Map<String, String>> getViewConfiguration(HelloWorldConfiguration configuration) {
                return configuration.getViewRendererConfiguration();
            }
        });
    }

    @Override
    public void run(HelloWorldConfiguration configuration, Environment env) {
        final PersonDAO personDAO = new PersonDAO(hibernateBundle.getSessionFactory());
        final TaskDAO taskDAO = new TaskDAO(hibernateBundle.getSessionFactory());
        final ProcessDAO processDAO = new ProcessDAO(hibernateBundle.getSessionFactory());
        processDAO.setTaskDAO(taskDAO);
        taskDAO.setProcessDAO(processDAO);
        final TaskConnectionDAO taskConnectionDAO = new TaskConnectionDAO(hibernateBundle.getSessionFactory());
        taskConnectionDAO.setProcessDAO(processDAO);
        taskConnectionDAO.setTaskDAO(taskDAO);
        final Template template = configuration.buildTemplate();

        env.healthChecks().register("template", new TemplateHealthCheck(template));
        env.jersey().register(DateRequiredFeature.class);
        env.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new ExampleAuthenticator())
                .setAuthorizer(new ExampleAuthorizer())
                .setRealm("SUPER SECRET STUFF")
                .buildAuthFilter()));
        env.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
        env.jersey().register(RolesAllowedDynamicFeature.class);
        env.jersey().register(new HelloWorldResource(template));
        env.jersey().register(new ViewResource());
        env.jersey().register(new ProtectedResource());
        env.jersey().register(new PeopleResource(personDAO));
        env.jersey().register(new PersonResource(personDAO));
        env.jersey().register(new ProcessResource(processDAO));
        env.jersey().register(new TaskResource(taskDAO));
        env.jersey().register(new TaskConnectionResource(taskConnectionDAO));
        GoJsResource goJsResource = new GoJsResource();
        goJsResource.setProcessDAO(processDAO);
        goJsResource.setTaskDAO(taskDAO);
        goJsResource.setTaskConnectionDAO(taskConnectionDAO);
        env.jersey().register(goJsResource);
        env.jersey().register(new FilteredResource());
    }
}
