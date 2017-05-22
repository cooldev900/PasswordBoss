package com.passwordboss.android.beans;

public class NotificationBean{

	/* Initialize Variables */
	private String secure_item_id, do_not_show, last_password_change,  last_alert, created_date,
	last_modified_date, last_access, access_count, item_type, site_id, site_name, image_id, imageUri, email, message, share_id;
	private boolean has_duplicate, is_weak;
	
	private String title, subtitle, description;
	
	public NotificationBean() {
		super();
	}

	public NotificationBean(String secure_item_id, String do_not_show,
			String last_password_change, boolean has_duplicate, boolean is_weak,
			String last_alert, String created_date, String last_modified_date,
			String last_access, String access_count, String item_type,
			String site_id, String site_name, String image_id, String imageUri) {
		super();
		this.secure_item_id = secure_item_id;
		this.do_not_show = do_not_show;
		this.last_password_change = last_password_change;
		this.has_duplicate = has_duplicate;
		this.is_weak = is_weak;
		this.last_alert = last_alert;
		this.created_date = created_date;
		this.last_modified_date = last_modified_date;
		this.last_access = last_access;
		this.access_count = access_count;
		this.item_type = item_type;
		this.site_id = site_id;
		this.site_name = site_name;
		this.image_id = image_id;
		this.imageUri = imageUri;
	}
	
	public NotificationBean(String secure_item_id, String last_alert, String create_date, String last_modified_date, 
			String item_type, String email,String message, String shareId) {
		super();
		this.secure_item_id = secure_item_id;
		this.last_alert = last_alert;
		this.created_date = create_date;
		this.last_modified_date = last_modified_date;
		this.item_type = item_type;
		this.email = email;
		this.message = message;
		this.share_id = shareId;
	}

	public String getSecureItemId() {
		return secure_item_id;
	}

	public void setSecureItemId(String secure_item_id) {
		this.secure_item_id = secure_item_id;
	}

	public String getDoNotShow() {
		return do_not_show;
	}

	public void setDoNotShow(String do_not_show) {
		this.do_not_show = do_not_show;
	}

	public String getLastPasswordChange() {
		return last_password_change;
	}

	public void setLastPasswordChange(String last_password_change) {
		this.last_password_change = last_password_change;
	}

	public boolean isHasDuplicate() {
		return has_duplicate;
	}

	public void setHasDuplicate(boolean has_duplicate) {
		this.has_duplicate = has_duplicate;
	}

	public boolean isWeak() {
		return is_weak;
	}

	public void setIsWeak(boolean is_weak) {
		this.is_weak = is_weak;
	}

	public String getLastAlert() {
		return last_alert;
	}

	public void setLastAlert(String last_alert) {
		this.last_alert = last_alert;
	}

	public String getCreatedDate() {
		return created_date;
	}

	public void setCreatedDate(String created_date) {
		this.created_date = created_date;
	}

	public String getLastModifiedDate() {
		return last_modified_date;
	}

	public void setLastModifiedDate(String last_modified_date) {
		this.last_modified_date = last_modified_date;
	}

	public String getLastAccess() {
		return last_access;
	}

	public void setLastAccess(String last_access) {
		this.last_access = last_access;
	}

	public String getAccessCount() {
		return access_count;
	}

	public void setAccessCount(String access_count) {
		this.access_count = access_count;
	}

	public String getItemType() {
		return item_type;
	}

	public void setItemType(String item_type) {
		this.item_type = item_type;
	}

	public String getSiteId() {
		return site_id;
	}

	public void setSiteId(String site_id) {
		this.site_id = site_id;
	}

	public String getSiteName() {
		return site_name;
	}

	public void setSiteName(String site_name) {
		this.site_name = site_name;
	}

	public String getImageId() {
		return image_id;
	}

	public void setImageId(String image_id) {
		this.image_id = image_id;
	}

	public String getImageUri() {
		return imageUri;
	}

	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getShareId() {
		return share_id;
	}

	public void setShareId(String share_id) {
		this.share_id = share_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((access_count == null) ? 0 : access_count.hashCode());
		result = prime * result + ((created_date == null) ? 0 : created_date.hashCode());
		result = prime * result + ((do_not_show == null) ? 0 : do_not_show.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + (has_duplicate ? 1231 : 1237);
		result = prime * result + ((imageUri == null) ? 0 : imageUri.hashCode());
		result = prime * result + ((image_id == null) ? 0 : image_id.hashCode());
		result = prime * result + (is_weak ? 1231 : 1237);
		result = prime * result + ((item_type == null) ? 0 : item_type.hashCode());
		result = prime * result + ((last_access == null) ? 0 : last_access.hashCode());
		result = prime * result + ((last_alert == null) ? 0 : last_alert.hashCode());
		result = prime * result + ((last_modified_date == null) ? 0 : last_modified_date.hashCode());
		result = prime * result + ((last_password_change == null) ? 0 : last_password_change.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((secure_item_id == null) ? 0 : secure_item_id.hashCode());
		result = prime * result + ((share_id == null) ? 0 : share_id.hashCode());
		result = prime * result + ((site_id == null) ? 0 : site_id.hashCode());
		result = prime * result + ((site_name == null) ? 0 : site_name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NotificationBean other = (NotificationBean) obj;
		if (access_count == null) {
			if (other.access_count != null)
				return false;
		} else if (!access_count.equals(other.access_count))
			return false;
		if (created_date == null) {
			if (other.created_date != null)
				return false;
		} else if (!created_date.equals(other.created_date))
			return false;
		if (do_not_show == null) {
			if (other.do_not_show != null)
				return false;
		} else if (!do_not_show.equals(other.do_not_show))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (has_duplicate != other.has_duplicate)
			return false;
		if (imageUri == null) {
			if (other.imageUri != null)
				return false;
		} else if (!imageUri.equals(other.imageUri))
			return false;
		if (image_id == null) {
			if (other.image_id != null)
				return false;
		} else if (!image_id.equals(other.image_id))
			return false;
		if (is_weak != other.is_weak)
			return false;
		if (item_type == null) {
			if (other.item_type != null)
				return false;
		} else if (!item_type.equals(other.item_type))
			return false;
		if (last_access == null) {
			if (other.last_access != null)
				return false;
		} else if (!last_access.equals(other.last_access))
			return false;
		if (last_alert == null) {
			if (other.last_alert != null)
				return false;
		} else if (!last_alert.equals(other.last_alert))
			return false;
		if (last_modified_date == null) {
			if (other.last_modified_date != null)
				return false;
		} else if (!last_modified_date.equals(other.last_modified_date))
			return false;
		if (last_password_change == null) {
			if (other.last_password_change != null)
				return false;
		} else if (!last_password_change.equals(other.last_password_change))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (secure_item_id == null) {
			if (other.secure_item_id != null)
				return false;
		} else if (!secure_item_id.equals(other.secure_item_id))
			return false;
		if (share_id == null) {
			if (other.share_id != null)
				return false;
		} else if (!share_id.equals(other.share_id))
			return false;
		if (site_id == null) {
			if (other.site_id != null)
				return false;
		} else if (!site_id.equals(other.site_id))
			return false;
		if (site_name == null) {
			if (other.site_name != null)
				return false;
		} else if (!site_name.equals(other.site_name))
			return false;
		return true;
	}
	
	
	
}