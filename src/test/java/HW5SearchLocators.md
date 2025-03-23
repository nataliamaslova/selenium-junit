#  Домашнее задание 5: Написание локаторов

1. Откройте страницу https://bonigarcia.dev/selenium-webdriver-java/web-form.html
2. Написать локаторы для всех элементов
3. Использовать лучшие локаторы
4. Задание со звездочкой: Напишите по локатору каждого типа минимум 1 раз

#### 1. Main header Hands-On Selenium WebDriver with Java
- .display-4
- h1.display-4
- //h1[@class='display-4']

#### 2. Logo img
- .img-fluid
   //img[@class='img-fluid']

#### 3. Practice site
- h5

#### 4. Dividing line
- .col.col-12
- //div[@class='col col-12']

#### 5. Web form
- display-6 
- h1.display-6
- //h1[@class='display-6']

#### 6. Text input
- #my-text-id
- //input[@id='my-text-id']

#### 7. Password
- [name='my-password']
- //input[@name='my-password']

#### 8. Textarea
- [name='my-textarea']
- //textarea[@name='my-textarea']

#### 9. Disabled input
- [name='my-disabled']
- //input[@name='my-disabled']

#### 10. Readonly input
- [name='my-readonly']
- //input[@name='my-readonly']

#### 11. Link
- [href='./index.html']
- a[href='./index.html']
- //a[contains(@href, 'index')]

#### 12. Dropdown (select)
- [name='my-select']
- //select[@name='my-select']

#### 13. Dropdown (datalist)
- [name='my-datalist']
- //input[@name='my-datalist']

#### 14. File input
- [name='my-file']
- //input[@name='my-file']

#### 15. Checked checkbox
- #my-check-1
- //input[@id='my-check-1']

#### 16. Default checkbox
- #my-check-2
- //input[@id='my-check-2']

#### 17. Checked radio
- #my-radio-1
- //input[@id='my-radio-1']

#### 18. Default radio
- #my-radio-2
- //input[@id='my-radio-2']

#### 19. Submit
- .btn.btn-outline-primary.mt-3
- //button[@type='submit']

#### 20. Color picker
- [name='my-colors']
- //input[@name='my-colors']

#### 21. Date picker
- [name='my-date']
- //input[@name='my-date']

#### 22. Example range
- [name='my-range']
- //input[@name='my-range']
