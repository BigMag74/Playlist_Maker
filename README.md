# Проект Playlist Maker

Данный проект представляет собой android-приложение для составления собственных плейлистов,
использующее [Itunes API](https://developer.apple.com/library/archive/documentation/AudioVideo/Conceptual/iTuneSearchAPI/index.html)

# Возможности приложения

Приложение предоставляет следующую функциональность:

- Поиск треков;
- Прослушивание треков;
- Добавление треков в список "Избранного";
- Создание плейлистов;
- Добавление треков в созданные плейлисты.

## Общяя информация

- Приложение поддерживает устройства, начиная с Android 6.0 (minSdkVersion = 23).
- Приложение поддерживает только портретную ориентацию (`portrait`), при перевороте экрана ориентация не меняется.
- Приложение поддерживает две темы: светлую и тёмную.
- Приложение поддерживает два языка: Русский и Английский.

## Главный экран -- Медиатека

Этот экран содержит две вкладки Избранные треки и плейлисты. 
Переключение между вкладка доступно как по нажатию, так и по свайпу.

### Вкладка Избранные треки

Здесь содержатся все треки, которые пользователь решил добавить в избранное.

Избранные треки хранятся в локальной базе данных. При отсутствии интернета список треков отобразится в том же виде,
за исключением обложки, вместо неё покажется плейсхолдер.
Треки отсортированы в порядке добавления их в избранное, самые новые отображатся вверху списка.

![Вкладка Избранные треки](https://github.com/BigMag74/Playlist_Maker/blob/main/QIP%20Shot%20-%20Screen%20039.png)

### Вкладка Плейлисты

Здесь отображаются созданные плейлисты, также здесь пользователь может перейти на экран создания нового плейлиста.

Плейлисты также хранятся в локальной базе данных вместе с обложкой и доступны даже без интернета.

![Вкладка плейлисты](https://github.com/BigMag74/Playlist_Maker/blob/main/QIP%20Shot%20-%20Screen%20040.png)

## Экран поиска треков

На этом экране пользователь может искать треки по любому непустому набору слов поискового запроса. 
Результаты поиска представляют собой список, содержащий краткую информацию о треках.
Поиск осуществляется по нажатию соответствующей клавиши на клавиатуре или автоматически спустя некоторое время, если поисковый запрос изменился хотя бы на один символ.

Если поле поискового запроса пусто, на экране отображается история последних прослушанных треков.
Данную историю можно очистить, нажав соотвествующую кнопку.

![Экран поиска треков](https://github.com/BigMag74/Playlist_Maker/blob/main/QIP%20Shot%20-%20Screen%20041.png)

## Экран прослушивания трека

Данный экран содержит подробное описание выбранного трека (название, исполнитель, длительность, обложка, альбом, год выхода, жанр, страна).
Также на этом экране пользователь может делать следующее:

- Прослушать данный трек;
- Добавить данный трек в избранное или удалить из него;
- Добавить данный трек в существующий плейлист;
- Перейти к созданию нового плейлиста.

![Экран прослушивания трека](https://github.com/BigMag74/Playlist_Maker/blob/main/QIP%20Shot%20-%20Screen%20042.png)

## Экран создания плейлиста

На данном экране пользователь может создать новый плейлист.
Для создания плейлиста достаточно указать только его названия, но пользователь также может выбрать обложку плейлиста и его описание.
Обложку можно выбрать из локальных файлов на телефоне.

![Экран создания плейлиста](https://github.com/BigMag74/Playlist_Maker/blob/main/QIP%20Shot%20-%20Screen%20043.png)

## Экран просмотра плейлиста

На данном экране пользователь видит всю информацию о плейлисте (название, описание, обложку, количество треков и суммарную продолжительность треков в плейлисте).
При отсутсвии обложки отображается соответствующий плейсхолдер.
Список треков расположен в bottomsheet контейнере и при необходимости его можно развернуть на весь экран.

На этом экране пользователь может поделиться плейлистом, редактировать данный плейлист или же удалить его.
Для удаления трека необходимо совершить длительное нажатие на необходимом треке, тогда появится диалоговое окно с подтверждением намерения.
При нажатии на кнопку редактирования плейлистра, пользователя перебрасывает на экран редактирования плейлиста, схожий по своей логике с экраном создания плейлиста.

![Экран просмотра плейлиста](https://github.com/BigMag74/Playlist_Maker/blob/main/QIP%20Shot%20-%20Screen%20044.png)

