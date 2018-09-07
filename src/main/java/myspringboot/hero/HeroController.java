package myspringboot.hero;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins="http://localhost:4200")
@RestController
@RequestMapping(value="/heroes")
public class HeroController {
	private List<Hero> heroes = new ArrayList<>();
	//Logger 객체 생성
	private final Logger logger = 
			LoggerFactory.getLogger(this.getClass());
			
	public HeroController() {
		buildHeroes();
	}
	
	private void buildHeroes() {
		  Hero hero11 = new Hero(11L,"Mr. Nice");
		  Hero hero12 = new Hero(12L,"Narco");
		  Hero hero13 = new Hero(13L,"Bombasto");
		  Hero hero14 = new Hero(14L,"Celeritas");
		  Hero hero15 = new Hero(15L,"Magneta");
		  Hero hero16 = new Hero(16L,"RubberMan");
		  Hero hero17 = new Hero(17L,"Dynama");
		  Hero hero18 = new Hero(18L,"DrIQ");
		  Hero hero19 = new Hero(19L,"Magma");
		  Hero hero20 = new Hero(20L,"Tornado");
		  
		  heroes.add(hero11);
		  heroes.add(hero12);
		  heroes.add(hero13);
		  heroes.add(hero14);
		  heroes.add(hero15);
		  heroes.add(hero16);
		  heroes.add(hero17);
		  heroes.add(hero18);
		  heroes.add(hero19);
		  heroes.add(hero20);
	}        
	
//	@RequestMapping(method=RequestMethod.GET)
//	public List<Hero> getHeroes() {
//		logger.debug("Hero List 요청됨!");
//		return this.heroes;
//	}
	
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public Hero getHero(@PathVariable("id") Long myid) {
		logger.info("Hero Detail 요청됨! " + myid);
		//1.stream() - List<Hero> -> Stream<Hero>
		//2.filter() - Stream<Hero>
		//3.findFirst() - Optional<Hero>
		//4.orElse - Hero 
		return this.heroes.stream()
				          .filter(hero -> hero.getId() == myid)
				          .findFirst()
				          .orElse(null);
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public Hero saveHero(@RequestBody Hero hero) {
		logger.info("Hero 등록 요청됨 :" + hero );
		Long nextId = 0L;
		if(this.heroes.size() != 0) {
			//맨 마지막에 있는 Hero 가져온다.
			//1.stream() - List<Hero> -> Stream<Hero>
			//2.skip(9) - Stream<Hero>
			//3.findFirst - Optional<Hero>
			//4.orElse - Hero
			Hero lastHero = this.heroes.stream()
					                   .skip(this.heroes.size()-1)
					                   .findFirst()
					                   .orElse(null);
			nextId = lastHero.getId() + 1;
		}
		hero.setId(nextId);
		this.heroes.add(hero);
		return hero;
	}
	
	@RequestMapping(value="/{id}",method=RequestMethod.PUT)
	public Hero updateHero(@PathVariable("id")Long id,@RequestBody Hero hero) {
		logger.info("Hero 수정 요청됨 :" + hero );
		Hero modifiedHero = 
				this.heroes.stream()
				           .filter(h -> h.getId() == id)
				           .findFirst().orElse(null);
		modifiedHero.setName(hero.getName());
		return modifiedHero;
	}
	
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public boolean deleteHero(@PathVariable("id")Long id) {
		logger.info("Hero 삭제 요청됨! " + id);
		Hero deletedHero = 
				this.heroes.stream()
		           .filter(h -> h.getId() == id)
		           .findFirst().orElse(null);
		if(deletedHero != null) {
			this.heroes.remove(deletedHero);
			return true;
		}else {
			return false;
		}
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Hero> searchHero(@RequestParam(required=false,defaultValue="") 
		String name){
		logger.debug("Hero 검색 요청됨! " + name);
		if (name.equals("")) {
			logger.debug("전체 조회");
			return this.heroes;
		}else {
			logger.debug("검색어로 조회");
			return this.heroes.stream()
					.filter(hero -> hero.getName().contains(name))
					.collect(Collectors.toList());
		}
	}
}
