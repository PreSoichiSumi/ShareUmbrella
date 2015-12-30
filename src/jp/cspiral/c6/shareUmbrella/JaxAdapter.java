package jp.cspiral.c6.shareUmbrella;

import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import jp.cspiral.c6.shareUmbrella.model.Account;
import jp.cspiral.c6.shareUmbrella.model.Session;
import jp.cspiral.c6.shareUmbrella.util.DBUtils;
import org.codehaus.jettison.json.JSONObject;
import exception.NotEnoughPointException;
import exception.NotFoundUmbrellaException;
import exception.NotOwnerException;
import exception.NotRentalException;
import exception.NotReturnException;

@Path("/")
public class JaxAdapter {

	private final AlpacaController controller = new AlpacaController();

	/**
	 * 傘を返す．<br>
	 *
	 * @author n-yuuta
	 * @param umbId 傘のID
	 *
	 */
	@GET
	@Produces({MediaType.APPLICATION_XML})
	@Path("/returnUmbrella")
	public Response returnUmbrella(@Context final HttpServletRequest servletRequest,
			@QueryParam("umbId") final String umbId,
			@QueryParam("longtitude") final double longtitude,
			@QueryParam("latitude") final double latitude) {

		//指定されたリクエストに対するセッションを取得．存在しなければnull
		final HttpSession session = servletRequest.getSession(false);
		if (session == null) {

			return Response.status(200).entity("<returnUmbrella>ログインしてください．</returnUmbrella>").build();
		}

		if (!Character.isDigit(umbId.charAt(0))) {
			return Response.status(200).entity("<returnUmbrella>QRコードの読み取りに失敗しました．再度お試しください．</returnUmbrella>").build();
		}

		String message = "";

		try {
			controller.returnUmbrella(Integer.valueOf(umbId),session.getAttribute("userId").toString(),longtitude,latitude);
			message = "返却に成功しました．";
		} catch (NumberFormatException e) {
			message=e.toString();
		} catch (NotFoundUmbrellaException e) {
			message=e.toString();
		} catch (NotOwnerException e) {
			message=e.toString();
		} catch (NotRentalException e) {
			message=e.toString();
		}
		return Response.status(200).entity("<returnUmbrella>"+message+"</returnUmbrella>").build();
	}

	/**
	 * 傘を借りる．<br>
	 *
	 * @author n-yuuta
	 * @param umbId 傘のID
	 *
	 */
	@GET
	@Produces({MediaType.APPLICATION_XML})
	@Path("/rentUmbrella")
	public Response rentUmbrella(@Context final HttpServletRequest servletRequest,
			@QueryParam("umbId") final String umbId) {

		//指定されたリクエストに対するセッションを取得．存在しなければnull
		final HttpSession session = servletRequest.getSession(false);
		if (session == null) {

			return Response.status(200).entity("<rentUmbrealla>ログインしてください．</rentUmbrealla>").build();
		}

		String message = "";

		try {
			controller.rentUmbrella(Integer.valueOf(umbId),session.getAttribute("userId").toString());
			message = "レンタルに成功しました．";
		} catch (NumberFormatException e) {
			message = e.getMessage();
		} catch (NotEnoughPointException e) {
			message = e.getMessage();
		} catch (NotFoundUmbrellaException e) {
			message = e.getMessage();
		} catch (NotReturnException e) {
			message = e.getMessage();
		}
		return Response.status(200).entity("<rentUmbrealla>"+message+"</rentUmbrealla>").build();
	}

	/**
	 * 施設をさがす．<br>
	 *
	 * @author n-yuuta
	 * @param
	 *
	 */
	@GET
	@Produces({MediaType.APPLICATION_XML})
	@Path("/searchBuilding")
	public Response searchBuilding(@Context final HttpServletRequest servletRequest,
			@QueryParam("longitude") double longitude,
			@QueryParam("latitude") double latitude) {

		//指定されたリクエストに対するセッションを取得．存在しなければnull
		final HttpSession session = servletRequest.getSession(false);
		if (session == null) {

			return Response.status(200).entity("<message>ログインしてください．</message>").build();
		}

		//return Response.status(200).entity(controller.searchBuilding(longitude,latitude)).build();
		return Response.status(200).entity(controller.searchBuildings(longitude,latitude)).build();
	}

	/**
	 * 施設をさがす．<br>
	 *
	 * @author n-yuuta
	 * @param
	 *
	 */
	@GET
	@Produces({MediaType.APPLICATION_XML})
	@Path("/searchOneBuilding")
	public Response searchOneBuilding(@Context final HttpServletRequest servletRequest,
			@QueryParam("longitude") double longitude,
			@QueryParam("latitude") double latitude) {

		//指定されたリクエストに対するセッションを取得．存在しなければnull
		final HttpSession session = servletRequest.getSession(false);
		if (session == null) {

			return Response.status(200).entity("<message>ログインしてください．</message>").build();
		}

		//return Response.status(200).entity(controller.searchBuilding(longitude,latitude)).build();
		return Response.status(200).entity(controller.searchBuilding(longitude,latitude)).build();
	}
	/**
	 * ログインする<br>
	 * 同時に，セッションを作成．<br>
	 *
	 */
	@POST
	@Produces({MediaType.APPLICATION_XML})
	@Path("/login")
	public Response login(@Context final HttpServletRequest servletRequest, @FormParam("userId") String userId,
			@FormParam("pass") String pass) {


		Account account = controller.login(userId, pass);
		if(account != null) {

			//ログインに成功したなら，セッションを作成
	        final HttpSession session = servletRequest.getSession(true);
	        session.setAttribute("userId",account.getUserId());
	        session.setAttribute("pass",account.getPass());
	        session.setAttribute("point",account.getPoint());
	        session.setAttribute("sessionId",account.getSessionId());

			return Response.status(200).entity("<login>success</login>").build();
		}

		return Response.status(200).entity("<login>fail</login>").build();
	}

	/**
	 * ログアウトする<br>
	 * 同時に，セッションを破棄．<br>
	 *
	 * @author n-yuuta
	 */
	@GET
	@Produces({MediaType.APPLICATION_XML})
	@Path("/logout")
	public Response logout(@Context final HttpServletRequest servletRequest) {

		//指定されたリクエストに対するセッションを取得．存在しなければnull
		final HttpSession session = servletRequest.getSession(false);
		if (session != null) {

			//セッションを破棄
			session.invalidate();
			return Response.status(200).entity("<logout>ログアウトしました．</logout>").build();
		} else {

			return Response.status(200).entity("<logout>ログインしてください．</logout>").build();
		}
	}

	/**
	 * ユーザ情報を取得する
	 * @author s-sumi
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("getUserInfo")
	public Response getUserInfo(@Context final HttpServletRequest servletRequest){

		//指定されたリクエストに対するセッションを取得．存在しなければnull
		final HttpSession session = servletRequest.getSession(false);
		if (session == null) {

			JSONObject o = new JSONObject();
			return Response.status(200).entity(o).build();
		}

		JSONObject o=controller.getUserInfo(session.getAttribute("userId").toString());
		return Response.status(200).entity(o).build();
	}

	/**
	 * ポイントをチャージする
	 * @param userId
	 * @param money
	 * @author s-sumi
	 * @return 実行結果を示す文字列
	 */
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/charge")
	public Response charge(@Context final HttpServletRequest servletRequest,
			@QueryParam("money") final String money){

		//指定されたリクエストに対するセッションを取得．存在しなければnull
		final HttpSession session = servletRequest.getSession(false);
		if (session == null) {

			return Response.status(200).entity("").build();
		}

		int intMoney=0;
		try {
			intMoney=Integer.valueOf(money).intValue();
		} catch (NumberFormatException e) {
			//handle exception
			return Response.status(200).entity("NumFormatException\n").build();
		}

		String str=controller.charge(session.getAttribute("userId").toString(),intMoney);
		return Response.status(200).entity(str).build();
	}

	/**
	 * 退会する
	 * @param userId
	 */
	@POST
	//@Produces({MediaType.application_})
	@Path("/leave")
	public Response leave(@Context final HttpServletRequest servletRequest) {
		URI uri;
		try {
			uri=new URI("../web/");
		} catch (Exception e) {
			return Response.status(200).entity("uriException").build();
		}

		//指定されたリクエストに対するセッションを取得．存在しなければnull
		final HttpSession session = servletRequest.getSession(false);
		if (session == null) {
			//return Response.status(200).entity("").build();
			return Response.seeOther(uri).build();
		}

		String str=controller.leave(session.getAttribute("userId").toString());

		//セッションを破棄
		session.invalidate();

		//	return Response.status(200).entity(str).build();
		return Response.seeOther(uri).build();
	}

	/**
	 * セッションを確認する
	 *
	 * @author n-yuuta
	 */
	@GET
	@Produces({MediaType.APPLICATION_XML})
	@Path("/confirmSession")
	public Response confirmSession(@Context final HttpServletRequest servletRequest) {

		//指定されたリクエストに対するセッションを取得．存在しなければnull
		final HttpSession session = servletRequest.getSession(false);

		Session sessionObject = new Session();
		if (session == null) {

			sessionObject.setUserId("");
			sessionObject.setPass("");
			sessionObject.setPoint(0);

		} else {

			sessionObject.setUserId(session.getAttribute("userId").toString());
			sessionObject.setPass(session.getAttribute("pass").toString());
			sessionObject.setPoint(Integer.valueOf(session.getAttribute("point").toString()));
		}

		return Response.status(200).entity(sessionObject).build();
	}

	/**
	 * ./api/ へのアクセスを ./api/application.wadl（APIの仕様書） にリダイレクトする
	 * @return
	 * @throws URISyntaxException
	 */
	@GET
	@Path("/")
	public Response redirect() throws URISyntaxException{
		URI uri = new URI("application.wadl");
		return Response.seeOther(uri).build();
	}

	/**
	 * 新規登録を行う
	 */
	@POST
	@Produces({MediaType.APPLICATION_XML})
	@Path("/signup")
	public Response signUp(@FormParam("userId") String userId, @FormParam("pass") String pass) {

		String message = controller.register(userId, pass);

		return Response.status(200).entity(message).build();

//		if(message == "OK") {
//			//新規登録成功
//			return Response.status(200).entity("OK").build();
//		}else if(message == "WID"){
//			//ID重複
//			return Response.status(200).entity("WID").build();
//		}
//
//		//その他?
//		return Response.status(200).entity("nothing").build();
	}

/////////////////////////////////////////////////////////////////////////////
	//デバッグ用API

	@GET
	@Path("/initDB")
	public Response initDB() {

		DBUtils.initDB();

		return Response.status(200).entity("ok").build();
	}

	@GET
	@Path("/addAccount")
	public Response addAccount(
			@QueryParam("userId") String userId,
			@QueryParam("pass") String pass,
			@QueryParam("point") int point) {

		DBUtils.addAccount(userId,pass,point);

		return Response.status(200).entity("ok").build();
	}

	@GET
	@Path("/addUmbrella")
	public Response addUmbrella(
			@QueryParam("umbId") int umbId,
			@QueryParam("status") String status,
			@QueryParam("owner") String owner,
			@QueryParam("type") String type,
			@QueryParam("buildId") int buildId) {

		DBUtils.addUmbrella(umbId,status,owner,type,buildId);

		return Response.status(200).entity("ok").build();
	}

	@GET
	@Path("/addBuilding")
	public Response addBuilding(
			@QueryParam("buildId") int buildId,
			@QueryParam("name") String name,
			@QueryParam("longitude") double longitude,
			@QueryParam("latitude") double latitude,
			@QueryParam("count") int count) {

		DBUtils.addBuilding(buildId,name,longitude,latitude,count);

		return Response.status(200).entity("ok").build();
	}
}