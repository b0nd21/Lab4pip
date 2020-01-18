package lab4;

import javax.ejb.EJB;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Secured
@Provider
public class AuthFilter implements ContainerRequestFilter {

    @Inject
    @AuthenticatedUser
    private Event<String> userAuthenticatedEvent;

    @EJB
    private UserService userService;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String token = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (token == null)
            throw new NotAuthorizedException("Authorization header must be provided");
        try {
            validateToken(token);
        } catch (Exception e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    private void validateToken(String token) {
        UserEntity userEntity = userService.findByToken(token);
        if (userEntity == null)
            throw new NotAuthorizedException("Bad auth token");
        userAuthenticatedEvent.fire(userEntity.getLogin());
    }
}
