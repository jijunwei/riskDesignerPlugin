package r06.propertiesWizards.testtable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class People{
	private Long id;
	private String name;
	private String sex;
	private Integer age;
	private Date createDate;
	public People(Long id,String name,String sex,Integer age,Date createDate){
		this.id = id;
		this.name = name;
		this.sex = sex;
		this.age = age;
		this.createDate = createDate;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public static List getPeople(){
		List list = new ArrayList();
		int n = 30;
		for(int i=1;i<n;i++){
			Long id = new Long(i);
			String name = "张"+i;
			String sex = "男";
			if((i%2) == 0){
				sex = "女";
			}
			Integer age;
			if(i<10){
				age = new Integer(i*10);
			}else if(i>10 && i<100){
				age = new Integer(i);
			}else if(i>100 && i<1000){
				age = new Integer(i/10);
			}else{
				age = new Integer(22);
			}
			Date createDate = new Date();
			People people = new People(id,name,sex,age,createDate);
			list.add(people);
		}
		return list;
	}
}