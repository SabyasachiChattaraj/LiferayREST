package test.portlet.portlet;

import com.liferay.portal.kernel.json.JSON;

@JSON(strict=true)
public class Post{

private Integer userId;
private Integer id;
private String title;
private String body;

@JSON(include=true)
public Integer getUserId() {
return userId;
}

public void setUserId(Integer userId) {
this.userId = userId;
}

@JSON(include=true)
public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}
@JSON(include=true)
public String getTitle() {
return title;
}

public void setTitle(String title) {
this.title = title;
}
@JSON(include=true)
public String getBody() {
return body;
}

public void setBody(String body) {
this.body = body;
}
 @Override
	public String toString() {
		return this.getTitle() + "== " + this.getBody();
	}

}