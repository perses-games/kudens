package com.persesgames.texture

/**
 * User: rnentjes
 * Date: 17-4-16
 * Time: 15:44
 */

val vertexShaderSource = """
    attribute vec2 a_position;

    attribute float a_imagesX;
    attribute float a_imagesY;
    attribute float a_currentImage;

    uniform mat4 u_projectionView;

    varying float v_imagesX;
    varying float v_imagesY;
    varying float v_currentImage;

    void main(void) {
        gl_Position = u_projectionView * vec4(a_position, -1, 1.0);
        gl_PointSize = 50.0 / gl_Position.w;

        v_imagesX = a_imagesX;
        v_imagesY = a_imagesY;
        v_currentImage = a_currentImage;
    }
"""

val fragmentShaderSource = """
    precision mediump float;

    uniform sampler2D uSampler;

    varying float v_imagesX;
    varying float v_imagesY;
    varying float v_currentImage;

    void main(void) {
        // calculate current texture coords depending on current image number
        float blockX = 1.0 / v_imagesX;
        float blockY = 1.0 / v_imagesY;

        float x = blockX * (mod(v_currentImage, v_imagesX));
        float y = blockY * floor(v_currentImage / v_imagesY);

        vec2 textCoord = vec2(x + blockX * gl_PointCoord.s, y + blockY - blockY * gl_PointCoord.t);
        //vec2 textCoord = vec2((x + blockX) * 0.0001 + gl_PointCoord.s, (y + blockY) * 0.0001 + gl_PointCoord.t);

        gl_FragColor = texture2D(uSampler, textCoord);
    }
"""

