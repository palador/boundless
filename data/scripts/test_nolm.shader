textures/test/steel
{
        qer_editorimage textures/test/steel.tga
        {
                map $lightmap
                rgbgen identity      
        }
        
        {
                map textures/test/steel.tga
                blendFunc GL_DST_COLOR GL_SRC_ALPHA
                rgbGen identity
                alphaGen lightingSpecular
        }
}

textures/test/wood
{
        qer_editorimage textures/test/wood.tga
        {
                map $lightmap
                rgbgen identity      
        }
        
        {
                map textures/test/wood.tga
                blendFunc GL_DST_COLOR GL_SRC_ALPHA
                rgbGen identity
                alphaGen lightingSpecular
        }
}
