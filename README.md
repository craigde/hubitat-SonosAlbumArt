# hubitat-SonosAlbumArt
 This is an application that allows you to select a sonos device and then creates a tile for use in dashboards with the album art for the playing track for use with HUBITAT

It has a dependency on the File Manager Device written by thebearmay so install that first from Hubitat Package Manager  

When you configure the app you pick a Sonos player for it to work with and connect it to the file manager device.
Then add an image tile to a dashboard and set it to http://your hub ip address/local/album.jpg with a refesh time configured (I used 5 sec)

Whenever a new track plays the app will fire and extract the album art from spotify and update it to the album.jpg file

<img width="794" alt="image" src="https://github.com/craigde/hubitat-SonosAlbumArt/assets/66229848/a2c06d82-5850-4573-a49b-3d133827526e">

