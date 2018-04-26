/* Copyright 2018 Esri
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.esri.arcgisruntime.samples.changefeaturelayerrenderer

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.esri.arcgisruntime.data.ServiceFeatureTable
import com.esri.arcgisruntime.geometry.Envelope
import com.esri.arcgisruntime.geometry.SpatialReferences
import com.esri.arcgisruntime.layers.FeatureLayer
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.Basemap
import com.esri.arcgisruntime.mapping.Viewpoint
import com.esri.arcgisruntime.symbology.SimpleLineSymbol
import com.esri.arcgisruntime.symbology.SimpleRenderer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  private var mFeatureLayer: FeatureLayer? = null

  private var overrideActive: Boolean = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    // set up the bottom toolbar
    createBottomToolbar()

    mapView.apply {
      // create a map with the topographic basemap
      map = ArcGISMap(Basemap.createTopographic()).apply {
        //set an initial viewpoint
        initialViewpoint = Viewpoint(
            Envelope(-1.30758164047166E7, 4014771.46954516, -1.30730056797177E7, 4016869.78617381,
                SpatialReferences.getWebMercator()))
        // create feature layer with its service feature table
        mFeatureLayer = FeatureLayer(ServiceFeatureTable(resources.getString(R.string.sample_service_url)))
        // add the layer to the map
        operationalLayers.add(mFeatureLayer)
      }
    }
  }

  private fun overrideRenderer() {
    // override the current renderer with a new simple line symbol renderer for the line feature layer
    mFeatureLayer?.renderer = SimpleRenderer(
        SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.rgb(0, 0, 255), 2f))
  }

  private fun resetRenderer() {
    // reset the renderer back to the definition from the source (feature service) using the reset renderer method
    mFeatureLayer?.resetRenderer()

  }

  private fun createBottomToolbar() {
    bottomToolbar.apply {
      inflateMenu(R.menu.menu_main)
      setOnMenuItemClickListener { item ->
        // handle action bar item clicks
        when {
          item.itemId == R.id.action_override_rend -> // check the state of the menu item
            when {
              !overrideActive -> {
                overrideRenderer()
                // change the text to reset
                overrideActive = true
                item.setTitle(R.string.action_reset)
              }
              else -> {
                resetRenderer()
                // change the text to override
                overrideActive = false
                item.setTitle(R.string.action_override_rend)
              }
            }
        }
        true
      }
    }
  }

  override fun onPause() {
    super.onPause()
    // pause MapView
    mapView.pause()
  }

  override fun onResume() {
    super.onResume()
    // resume MapView
    mapView.resume()
  }

  override fun onDestroy() {
    super.onDestroy()
    // dispose MapView
    mapView.dispose()
  }
}
