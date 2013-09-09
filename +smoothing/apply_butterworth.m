function Y = apply_butterworth(data, order, cutoff)
%%%function Y = apply_butterworth(data, order, cutoff)



[b, a] = butter(order, cutoff);
Hd = dfilt.df1(b, a);
Y = smoothing.apply_offset_filter(data, Hd);
