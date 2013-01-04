textures/test/steel
{
        qer_editorimage textures/test/steel.png
        {
                map $lightmap
                rgbgen identity      
        }
        
        {
                map textures/test/steel.png
                blendFunc GL_DST_COLOR GL_SRC_ALPHA
                rgbGen identity
                alphaGen lightingSpecular
        }
}

textures/test/wood
{
        qer_editorimage textures/test/wood.png
        {
                map $lightmap
                rgbgen identity      
        }
        
        {
                map textures/test/wood.png
                blendFunc GL_DST_COLOR GL_SRC_ALPHA
                rgbGen identity
                alphaGen lightingSpecular
        }
}
