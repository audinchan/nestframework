/**
 * 
 */
package ${hss_base_package}.webapp.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ${hss_base_package}.common.Constant;

/**
 * @author yjf
 *
 */
public class RandomCodeServlet extends HttpServlet
{

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 5916019367240722024L;
    
    private int width = 60;
    
    private int height = 20;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        
        Random random = new Random();
        
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        
        Font font = new Font("Times New Roman", Font.PLAIN, 18);
        g.setFont(font);
        
        // 边框
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, width - 1, height - 1);
        
        // 干扰线
        g.setColor(Color.GRAY);
        for (int i = 0; i < 60; i++)
        {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int x1 = random.nextInt(12);
            int y1 = random.nextInt(12);
            g.drawLine(x, y, x + x1, y + y1);
        }
        
        // 保存验证码
        StringBuffer randomCode = new StringBuffer();
        int red = 0;
        int green = 0;
        int blue = 0;
        
        // 生成验证码
        for (int i = 0; i < 4; i++)
        {
            String strRand = String.valueOf(random.nextInt(10));
            
            red = random.nextInt(110);
            green = random.nextInt(50);
            blue = random.nextInt(50);
            
            g.setColor(new Color(red, green, blue));
            g.drawString(strRand, 13 * i + 6, 16);
            
            randomCode.append(strRand);
        }
        
        // 验证码放入session中
        HttpSession session = req.getSession();
        //需要验证的地方
        session.setAttribute(Constant.KEY_LOGIN_CHECKCODE, randomCode.toString());
       
        // 禁止图象缓存
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expires", 0);
        
        resp.setContentType("image/jpeg");
        
        // 输出图象
        ServletOutputStream sos = resp.getOutputStream();
        ImageIO.write(img, "jpeg", sos);
        sos.close();
    }

}