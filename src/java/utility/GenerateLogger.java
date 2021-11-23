package utility;

import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

@Dependent		// CDI Bean
public class GenerateLogger {
    @Inject		// インジェクト先の情報を取得
    InjectionPoint point;
    @Produces	// 戻り値のオブジェクトを任意の管理Beanへインジェクト
    public Logger getLogger() {
        String className = point.getMember().getDeclaringClass().getName();
	// 取得したクラス名を引数にしてロガーを生成
        Logger logger = Logger.getLogger(className);
        return logger;
   }
}
