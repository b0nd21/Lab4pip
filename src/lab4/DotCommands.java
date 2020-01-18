package lab4;


import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/dots")
@RequestScoped
public class DotCommands {

    @EJB
    private DotService service;

    @Inject
    @AuthenticatedUser
    private UserEntity userEntity;

    @PostConstruct
    public void init(){
        userEntity = new UserEntity(userEntity.getLogin(),userEntity.getPasswordHash(),userEntity.getAuthToken());
    }

    @Secured
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("add")
    public Response add(Dot dot){
        DotEntity dotEntity = new DotEntity(dot.getX(),dot.getY(),dot.getR(),AreaCheck.isInArea(dot), userEntity);
        service.save(dotEntity);
        return Response.ok(dotEntity).build();
    }

    @Secured
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getAll")
    public Response getAll(){
        return Response.ok(service.getDots(userEntity)).build();
    }
}
