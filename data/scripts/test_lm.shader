textures/test/steel
{
        qer_editorimage textures/test/steel.tga
	q3map_lightmapSize 1024 1024
	q3map_lightmapBrightness 2.0
        {
		tcgen lightmap
                map $lightmap
                rgbgen identity      
        }
        
        {
                map textures/test/steel.tga
                blendFunc filter
        }
}

textures/test/wood
{
        qer_editorimage textures/test/wood.tga	
	q3map_lightmapSize 1024 1024
	q3map_lightmapBrightness 2.0
        {
		tcgen lightmap
                map $lightmap
                rgbgen identity      
        }
        
        {
                map textures/test/wood.tga
                blendFunc filter
        }
}
