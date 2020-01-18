package lab4;


import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.AuthenticationException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;

@Path("/users")
@Stateless
public class UserCommands {
    @EJB
    private UserService userService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("login")
    public Response login(User user){
        String login = user.getLogin();
        String password = user.getPassword();
        try{
            if (login == null || password == null)
                return Response.status(Response.Status.BAD_REQUEST).entity(new Message("No login or password!")).build();
            auth(login,hash(password));
            String token = createToken(login);
            return Response.ok(new Message(token)).build();
        } catch (Exception e){
            return  Response.status(Response.Status.UNAUTHORIZED).entity(new Message("Wrong login or password!")).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("register")
    public Response register(User user){
        String login = user.getLogin();
        String password = user.getPassword();
            if (login == null || password == null)
                return Response.status(Response.Status.BAD_REQUEST).entity(new Message("No login or password!")).build();
            if (userService.findByLogin(login)!=null)
                return Response.status(Response.Status.CONFLICT).entity(new Message("User exists!")).build();
            String passwordHash = hash(password);
            UserEntity userEntity = new UserEntity(login,passwordHash,null);
            userService.updateUser(userEntity);
            return Response.ok(new Message("User registered!")).build();
    }

    @Secured
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("logout")
    public Response logout(@HeaderParam("Authorization") String authorization) {
        userService.deleteToken(userService.findByToken(authorization).getLogin());
        return Response.ok(new Message("Logged out!")).build();
    }

    private void auth(String login, String password) throws AuthenticationException{
        UserEntity userEntity = userService.findByLogin(login);
        if(userEntity==null)
            throw new AuthenticationException("Wrong username!");
        if(!userEntity.getPasswordHash().equals(password))
            throw new AuthenticationException("Wrong password!");
    }

    private String hash(String input){
        String res="";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            res=hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return res;
    }

    private String createToken(String login){
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[10];
        random.nextBytes(bytes);
        String token = hash(bytes.toString()+ LocalDateTime.now());
        UserEntity userEntity = userService.findByLogin(login);
        userEntity.setAuthToken(token);
        userService.updateUser(userEntity);
        return token;
    }
}
