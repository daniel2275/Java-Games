#version 100

#ifdef GL_ES
precision mediump float;
#endif

// Passed in values from Java
uniform float u_time; // effect elapsed time
uniform vec2 u_resolution;
uniform float u_alphaValue;

uniform sampler2D u_texture;

void main()
{
    float time = u_time;

    vec2 p = gl_FragCoord.xy / u_resolution.xy;

    p.x += sin(p.y * 15.0 + u_time * 2.0) / 800.0;
    p.y += cos(p.x * 10.0 + u_time * 2.0) / 900.0;

    p.x += sin(p.y * 15.0 + u_time * 2.0) / (100.0 * (2.0 + sin(u_time)));
    p.y += cos(p.x * 10.0 + u_time * 2.0) / (200.0 * (2.0 + sin(u_time)));

    // Flip back
    // p.y = 1.0 - p.y;

//    gl_FragColor = texture2D(u_texture, p);
    vec4 texColor = texture2D(u_texture, p);
    texColor.rgb *= 1.0;
    gl_FragColor = vec4(texColor.rgb, texColor.a * u_alphaValue); // Apply transparency

}

