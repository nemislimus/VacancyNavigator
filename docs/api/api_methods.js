
Получение списка стран https://api.hh.ru/areas/countries
Возвращает массив(List)
[{
	"id": "113",
	"name": "Россия",
	"url": "https://api.hh.ru/areas/113"
}, {
	"id": "2112",
	"name": "Абхазия",
	"url": "https://api.hh.ru/areas/2112"
}, {
	"id": "5",
	"name": "Украина",
	"url": "https://api.hh.ru/areas/5"
}, {
	"id": "6",
	"name": "Австралия",
	"url": "https://api.hh.ru/areas/6"
}, {
	"id": "40",
	"name": "Казахстан",
	"url": "https://api.hh.ru/areas/40"
}, {
	"id": "7",
	"name": "Австрия",
	"url": "https://api.hh.ru/areas/7"
},
...
]

Получение списка индустрий https://api.hh.ru/industries
Возвращает массив(List)
[{
	"id": "7",
	"name": "Информационные технологии, системная интеграция, интернет",
	"industries": [{
		"id": "7.538",
		"name": "Интернет-провайдер"
	}, {
		"id": "7.539",
		"name": "Системная интеграция,  автоматизации технологических и бизнес-процессов предприятия, ИТ-консалтинг"
	}, {
		"id": "7.540",
		"name": "Разработка программного обеспечения"
	}, {
		"id": "7.541",
		"name": "Интернет-компания (поисковики, платежные системы, соц.сети, информационно-познавательные и развлекательные ресурсы, продвижение сайтов и прочее)"
	}]
}, {
  	"id": "36",
  	"name": "Государственные организации",
  	"industries": [{
  		"id": "36.403",
  		"name": "Государственные организации"
  	}]
},{
  	"id": "389",
  	"name": "Управление многопрофильными активами",
  	"industries": [{
  		"id": "389.530",
  		"name": "Управляющая компания группы, холдинга, штаб-квартира"
  	}]
 },
 ......
 ]
Получение списка регионов https://api.hh.ru/areas
Выдача верхнего уровня
[{
		id: '113',
		parent_id: null,
		name: 'Россия',
		areas: [
		    {
            	id: '1982',
            	parent_id: '113',
            	name: 'Чукотский АО',
            	areas: [
            	    {
                		"id": "219",
                		"parent_id": "1982",
                		"name": "Анадырь",
                		"areas": []
                	}, {
                		"id": "7563",
                		"parent_id": "1982",
                		"name": "Анюйск",
                		"areas": []
                	}, {
                		"id": "7486",
                		"parent_id": "1982",
                		"name": "Беринговский",
                		"areas": []
                	},
                	........
            	]
            },
            .....
		]
	},
	{
		id: '5',
		parent_id: null,
		name: 'Украина',
		areas: Array(21)
	},
	{
		id: '40',
		parent_id: null,
		name: 'Казахстан',
		areas: Array(193)
	},
	{
		id: '9',
		parent_id: null,
		name: 'Азербайджан',
		areas: Array(53)
	},
	{
		id: '16',
		parent_id: null,
		name: 'Беларусь',
		areas: Array(12)
	},
	{
		id: '28',
		parent_id: null,
		name: 'Грузия',
		areas: Array(26)
	},
	{
		id: '1001',
		parent_id: null,
		name: 'Другие регионы',
		areas: [
		    {
            	"id": "2112",
            	"parent_id": "1001",
            	"name": "Абхазия",
            	"areas": []
            }, {
            	"id": "6",
            	"parent_id": "1001",
            	"name": "Австралия",
            	"areas": []
            }, {
            	"id": "7",
            	"parent_id": "1001",
            	"name": "Австрия",
            	"areas": []
            }, {
            	"id": "2357",
            	"parent_id": "1001",
            	"name": "Албания",
            	"areas": []
            }, {
            	"id": "2368",
            	"parent_id": "1001",
            	"name": "Алжир",
            	"areas": []
            }, {
            	"id": "2376",
            	"parent_id": "1001",
            	"name": "Ангола",
            	"areas": []
            },
            ....
		]
	},
	{
		id: '48',
		parent_id: null,
		name: 'Кыргызстан',
		areas: Array(46)
	},
	{
		id: '97',
		parent_id: null,
		name: 'Узбекистан',
		areas: Array(116)
	}
]
