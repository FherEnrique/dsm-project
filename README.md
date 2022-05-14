# App Wallet #


![Build](https://travis-ci.org/laravel/framework.svg "Build Status")
![MIT](https://img.shields.io/packagist/l/laravel/framework "License")


![Logo](app/src/main/res/mipmap-xxhdpi/ic_launcher_round.png "Logo")

Crear una aplicación para llevar el registro de gastos e ingresos que puede tener una
persona natural a lo largo de la semana, clasificando según sus tipos de gastos para que
se puedan crear gráficas que muestren el uso de los ingresos del usuario y los
destinatarios de los mismos.

## Descripción ##

El problema se centra en las personas que no conllevan un orden financiero en sus
cuentas y gastos, con la aplicación se resuelve el desorden del usuario y organiza sus
cuentas y documenta todas las transacciones realizadas por el usuario teniendo un
control más detallado con reportes y gráficos estadísticos.

## Dependencias ##

------
```
dependencies {
    ...
    implementation 'androidx.appcompat:appcompat:1.2.0'
    implementation 'com.google.android.material:material:1.3.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.0.4'
    implementation 'com.google.firebase:firebase-auth:20.0.3'
    implementation 'com.google.android.gms:play-services-auth:19.0.0'
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'

    //add new implementation
    implementation 'androidx.recyclerview:recyclerview:1.1.0'
    implementation 'com.squareup.picasso:picasso:2.71828'
    implementation 'com.firebaseui:firebase-ui-firestore:7.1.1'
    implementation 'com.google.firebase:firebase-firestore:22.1.2'
    implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'

}
```


## Desarrolladores ##

* Fher Enrique Climaco Escamilla            CE171974  -  T01

## Licencias Creative Commons ##

![CC](https://co.creativecommons.net/wp-content/uploads/sites/27/2008/02/by-nc.png "CC-BY-NC")

CC-BY-NC: permite distribuir y hacer cambios en la obra siempre y cuando se incluya el
nombre del autor y la licencia. Sin embargo, no se permite su uso con fines comerciales.

## Otras Licencias ##

-------

    Copyright 2014 - 2020 Henning Dodenhof

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

