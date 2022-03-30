package biketour.update;

public class PasswordUpdateForm {

	private String newPassword;
	private String newPasswordConfirm;

	public PasswordUpdateForm(String newPassword, String newPasswordConfirm) {
		this.newPassword = newPassword;
		this.newPasswordConfirm = newPasswordConfirm;
	}


	public String getNewPassword() {
		return newPassword;
	}

	public String getNewPasswordConfirm() {
		return newPasswordConfirm;
	}
}
