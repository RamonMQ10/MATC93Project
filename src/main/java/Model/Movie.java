package Model;

public class Movie {
	
	String name;
	String genre;
	String country; 
	String varAbstract; 
	String budget; 
	String releaseDate;
	String runtime; 
	String alternateTitle; 
	String starring;
	String director; 
	String producer;
	
	public Movie(String name, String genre, String country, String varAbstract, String budget, String releaseDate,
			String runtime, String alternateTitle, String starring, String director, String producer) {
		super();
		this.name = name;
		this.genre = genre;
		this.country = country;
		this.varAbstract = varAbstract;
		this.budget = budget;
		this.releaseDate = releaseDate;
		this.runtime = runtime;
		this.alternateTitle = alternateTitle;
		this.starring = starring;
		this.director = director;
		this.producer = producer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getVarAbstract() {
		return varAbstract;
	}

	public void setVarAbstract(String varAbstract) {
		this.varAbstract = varAbstract;
	}

	public String getBudget() {
		return budget;
	}

	public void setBudget(String budget) {
		this.budget = budget;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getRuntime() {
		return runtime;
	}

	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}

	public String getAlternateTitle() {
		return alternateTitle;
	}

	public void setAlternateTitle(String alternateTitle) {
		this.alternateTitle = alternateTitle;
	}

	public String getStarring() {
		return starring;
	}

	public void setStarring(String starring) {
		this.starring = starring;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getProducer() {
		return producer;
	}

	public void setProducer(String producer) {
		this.producer = producer;
	}
	

}
