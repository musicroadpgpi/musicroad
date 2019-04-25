package com.nullpoint.musicroad.web.rest.vm;

import com.nullpoint.musicroad.service.dto.UserDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import com.nullpoint.musicroad.domain.City;
import com.nullpoint.musicroad.domain.enumeration.Genre;

/**
 * View Model extending the UserDTO, which is meant to be used in the user management UI.
 */
public class ManagedUserVM extends UserDTO {

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
	private String password;
	private String bandName;
	private String bio;
	private String coverPicture;
    private Integer componentNumber;
    private Integer creationYear;
    private Genre genre;
    private City city;

    public ManagedUserVM() {
        // Empty constructor needed for Jackson.
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public Integer getComponentNumber() {
		return componentNumber;
	}

	public void setComponentNumber(Integer componentNumber) {
		this.componentNumber = componentNumber;
	}

	public Integer getCreationYear() {
		return creationYear;
	}

	public void setCreationYear(Integer creationYear) {
		this.creationYear = creationYear;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
    }

    public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
    }
    public String getCoverPicture() {
		return coverPicture;
	}


    @Override
    public String toString() {
        return "ManagedUserVM{" +
            "} " + super.toString();
    }

	/**
	 * @return the bandName
	 */
	public String getBandName() {
		return bandName;
	}

	/**
	 * @param bandName the bandName to set
	 */
	public void setBandName(String bandName) {
		this.bandName = bandName;
	}

	/**
	 * @param coverPicture the coverPicture to set
	 */
	public void setCoverPicture(String coverPicture) {
		this.coverPicture = coverPicture;
	}
}
